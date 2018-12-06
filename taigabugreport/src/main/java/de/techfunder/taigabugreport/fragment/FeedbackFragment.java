package de.techfunder.taigabugreport.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import com.nguyenhoanglam.imagepicker.model.Image;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.gson.GsonBuilder;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.techfunder.taigabugreport.R;
import de.techfunder.taigabugreport.adapter.flexible.FlexibleViewClickListener;
import de.techfunder.taigabugreport.items.AttachmentItem;
import de.techfunder.taigabugreport.network.TaigaCallback;
import de.techfunder.taigabugreport.network.IBugReportApi;
import de.techfunder.taigabugreport.pojo.BugReportParams;
import de.techfunder.taigabugreport.pojo.reponse.BaseResponse;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RuntimePermissions
public class FeedbackFragment extends BaseFragment implements FlexibleViewClickListener {

    public static final int REQUEST_CODE_PICK_PHOTO = 101;
    public static final int PHOTO_LIMIT = 9;

    EditText etTitle;
    EditText etDescription;
    LinearLayout llAttachments;
    RecyclerView rvAttachments;
    Button btnAddMore;

    BugReportParams mBugReportParams;

    private FlexibleAdapter<IFlexible> mAdapter;
    private ArrayList<Image> selected = new ArrayList<>();

    public static FeedbackFragment newInstance(Bundle bundle) {
        FeedbackFragment fragment = new FeedbackFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.taigabugreport_fragment_feedback;
    }

    @Override
    public void onCreateFragment(Bundle instance) {
        if (getArguments() != null) {
            mBugReportParams = (BugReportParams) getArguments().getSerializable("bug_report_params");
        }
    }

    @Override
    public void onCreateViewFragment(View view) {
        mAdapter = new FlexibleAdapter<>(null);

    }

    @Override
    public void onViewCreatedFragment(View view, Bundle savedInstanceState) {
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        llAttachments = view.findViewById(R.id.llAttachments);
        rvAttachments = view.findViewById(R.id.tvAttachments);
        btnAddMore = view.findViewById(R.id.btnAddMore);
        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        rvAttachments.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAttachments.setAdapter(mAdapter);

        Button btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndSendReport();
            }
        });

        Drawable mDrawable = getContext().getResources().getDrawable(R.drawable.techfunder_ic_add_green);
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(getContext().getResources().getColor(R.color.taigabugreport_add_drawable_color), PorterDuff.Mode.SRC_ATOP));
        btnAddMore.setCompoundDrawablesWithIntrinsicBounds(mDrawable, null, null, null);
    }

    @Override
    public void onAttachFragment(Context context) {

    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    void openImagePicker() {
        Intent intent = ImagePicker.with(this)
                .setFolderMode(false) // folder mode (false by default)
                .setMaxSize(PHOTO_LIMIT - selected.size()) // single mode
                .setShowCamera(true) // show camera or not (true by default)
                .getIntent(); // start image picker activity with request code
        this.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PHOTO && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images != null) {
                addImages(images);
                //filepath = images.get(0).getPath();
                //ImageHelper.showImageFromFile(filepath, ivPhoto, 0);
            }
        }
    }

    private void addImages(ArrayList<Image> images) {
        for (Image item : images) {
            if (selected.size() == PHOTO_LIMIT) {
                showToast("Limit");
                break;
            } else {
                selected.add(item);

                AttachmentItem attachmentItem = new AttachmentItem(item, this);
                mAdapter.addItem(attachmentItem);
            }
        }
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        btnAddMore.setVisibility(selected.size() == PHOTO_LIMIT ? View.INVISIBLE : View.VISIBLE);
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FeedbackFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onFlexibleViewClick(View view, int position) {
        if(view.getId() == R.id.ibDelete)
        {
            mAdapter.removeItem(position);
            selected.remove(position);
            updateButtonVisibility();
        }
    }

    private void checkAndSendReport() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(description) || !selected.isEmpty()) {
            hideSoftKeyboard();
            RequestBody tokenPart = RequestBody.create(MediaType.parse("text/plain"), mBugReportParams.getToken());
            RequestBody projectPart = RequestBody.create(MediaType.parse("text/plain"), mBugReportParams.getProjectId());
            RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), mBugReportParams.getDescription(description, getContext()));
            RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), mBugReportParams.getTitle(title, getContext()));

            showProgressDialog();
            createClient().sendReport(mBugReportParams.getRequestSettings().getSendReportUrl(),
                    tokenPart, descriptionPart, titlePart, projectPart,
                    getFilePart(0),
                    getFilePart(1),
                    getFilePart(2),
                    getFilePart(3),
                    getFilePart(4),
                    getFilePart(5),
                    getFilePart(6),
                    getFilePart(7),
                    getFilePart(8)
            )
                    .enqueue(new TaigaCallback<BaseResponse>() {
                        @Override
                        public void onSuccess(Call<BaseResponse> call, Response<BaseResponse> response) {
                            closeProgressDialog();
                            showToast(R.string.taigabugreport_toast_thank_for_report);
                            getActivity().finish();
                        }

                        @Override
                        public void onServerError(String error) {
                            showToast(error);
                            closeProgressDialog();
                        }

                        @Override
                        public void onError(Call<BaseResponse> call, Throwable t) {
                            showToast(R.string.taigabugreport_toast_server_error);
                            closeProgressDialog();
                        }
                    });
        } else {
            showToast(R.string.taigabugreport_toast_nothing_send);
        }
    }

    private MultipartBody.Part getFilePart(int position) {
        if (selected.size() > position) {
            File file = new File(selected.get(position).getPath());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData(String.format(Locale.getDefault(), "file%d", position + 1), file.getName(), reqFile);
        } else {
            return null;
        }
    }

    private boolean validateEt(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            editText.setError(getString(R.string.taigabugreport_error_empty_field));
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    private IBugReportApi createClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .method(original.method(), original.body())
                                .build();
                        Log.d("OkHttp", request.headers().toString());
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBugReportParams.getRequestSettings().getServerUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls()
                                .create())
                )
                .build();

        return retrofit.create(IBugReportApi.class);
    }
}
