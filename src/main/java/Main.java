import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;


import java.io.IOException;

public class Main {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String[] searchStrings = {"data", "analytics"};
    private static String[] nocCodes = {};

    public static void main(String[] args) throws Exception {
        for (String q : searchStrings) {
            System.out.println("Searching for: " + q);
            Request request = new Request.Builder()
                    .url("https://www.jobbank.gc.ca/core/ta-jobtitle_en/select?q=" + q + "&wt=json&fq=noc_job_title_type_id:1&fl=title,noc_job_title_concordance_id,noc21_code")
                    .build();
            System.out.println(request.url());
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


                assert response.body() != null;
                String jsonString = response.body().string();
                System.out.println(jsonString);
                JSONObject coderollsJSONObject = new JSONObject(jsonString);
                System.out.println("Jsonresp:" + coderollsJSONObject.getJSONObject("response").getJSONArray(""));

            }
        }
    }
}


