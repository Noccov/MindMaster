package net.hoccob.mindmaster.server;

import android.net.TrafficStats;
import android.os.AsyncTask;

import net.hoccob.mindmaster.Player;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SendNickname extends AsyncTask<String, String, String> {

    private String url;
    private Player player;
    public SendNickname.AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public SendNickname(Player player, AsyncResponse delegate){
        this.delegate = delegate;
        this.player = player;
        TrafficStats.setThreadStatsTag(10000);
    }

    @Override
    protected String doInBackground(String... params) throws RuntimeException{
        //this.player.setUserName(params[0]);
        url =  "http://mindmaster.ee:8080/api/";
        String result = "";

        //Create template
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        //GET player by userName
        try {
            JSONObject jsonPlayer = new JSONObject();
            jsonPlayer.put("userName",player.getUserName());
            jsonPlayer.put("nickname", player.getNickname());
            //Configure for PUT request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonPlayer.toString(), headers);
            restTemplate.put(url + "users/{userId}", entity, player.getId());
            //result = restTemplate.getForObject(url, String.class, player.getUserName());
        }catch(RuntimeException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("NICKNAME SEND DONE!!");
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        delegate.processFinish(result);
    }
}
