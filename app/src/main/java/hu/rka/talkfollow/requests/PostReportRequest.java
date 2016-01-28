package hu.rka.talkfollow.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import hu.rka.talkfollow.models.UploadReport;
import hu.rka.talkfollow.network.ContentInterface;
import hu.rka.talkfollow.results.ReportResult;

/**
 * Created by Réka on 2016.01.27..
 */
public class PostReportRequest extends RetrofitSpiceRequest<ReportResult, ContentInterface> {

    private UploadReport uploadReport;

    public PostReportRequest(UploadReport uploadReport) {
        super(ReportResult.class, ContentInterface.class);
        this.uploadReport = uploadReport;
    }

    @Override
    public ReportResult loadDataFromNetwork() throws Exception {
        return getService().report(uploadReport);
    }
}
