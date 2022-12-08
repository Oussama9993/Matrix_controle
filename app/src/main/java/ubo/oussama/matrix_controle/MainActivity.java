package ubo.oussama.matrix_controle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements MqttCallback {

    private MqttAndroidClient client;
    private IMqttToken token;
    private static String Topic = "/matrice1";
    private String broker = "tcp://broker.hivemq.com:1883";
    private String clientId = MqttClient.generateClientId();

    @Override
    public void connectionLost(Throwable cause) {
    }
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        if(topic.equals(Topic)) {
//            textView1.setText(message.toString());
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            Log.i("LSE", token.getMessage().toString());
            Log.i("LSE", token.getResponse().toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void envoyer(String payload) {
        try {
            byte [] encodedPayload = payload.getBytes();
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(Topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    //private String clientId = "BxEad-23245";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connecter();
    }
    public void connecter() {
        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
//            connOpts.setUserName("");
//            connOpts.setPassword("".toCharArray());
            client = new MqttAndroidClient(this, broker, clientId);
            token = client.connect(connOpts);
            souscrire();
        } catch (MqttException e) {}
    }

    public void souscrire() {
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    client.setCallback(MainActivity.this);
                    client.subscribe(Topic, 0);
                    client.subscribe(Topic, 0);
                } catch (MqttException e) {}
            }
            @Override
            public void onFailure(IMqttToken token, Throwable exception) {}
        });
    }

    public void haut(View view) {
        envoyer("h");

    }

    public void bas(View view) {
        envoyer("b");
    }

    public void droite(View view) {
        envoyer("d");
    }

    public void gauche(View view) {
        envoyer("g");
    }
}