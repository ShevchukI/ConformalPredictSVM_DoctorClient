package com.controllers.requests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

/**
 * Created by Admin on 13.01.2019.
 */
public class SpecializationController extends MainController {

    public static HttpResponse getAllSpecialization(){
        String url = getUrl() + "/specializations";
        HttpGet request = new HttpGet(url);
        HttpResponse response = crudEntity(null, null, request, null, null);
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(getUrl() + "/specializations");
//        HttpResponse response = null;
//        try {
//            response = client.execute(request);
//        } catch (HttpHostConnectException e) {
//            HttpResponseFactory httpResponseFactory = new DefaultHttpResponseFactory();
//            response = httpResponseFactory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_GATEWAY_TIMEOUT, null), null);
//        }
        return response;
//        ArrayList<Specialization> specializations;
//        if (response.getStatusLine().getStatusCode() == 200){
//            StringBuilder stringBuilder = new StringBuilder();
//            DataInputStream dataInputStream = new DataInputStream(response.getEntity().getContent());
//            String line;
//            while ((line = dataInputStream.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            dataInputStream.close();
//            String json = stringBuilder.toString();
//            Gson gson = new Gson();
//            Type founderListType = new TypeToken<ArrayList<Specialization>>() {
//            }.getType();
//           specializations = gson.fromResponse(json, founderListType);
//        } else {
//            specializations = new ArrayList<Specialization>();
//            specializations.add(new Specialization(-2, "Empty"));
//        }
//        return specializations;
    }
}
