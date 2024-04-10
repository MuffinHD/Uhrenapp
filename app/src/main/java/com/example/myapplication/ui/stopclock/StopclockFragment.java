package com.example.myapplication.ui.stopclock;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentStopclockBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StopclockFragment extends Fragment {

    private FragmentStopclockBinding binding;
    private Button buttonStart;
    private Button buttonStop;
    private TextView stopClockView;
    private SimpleDateFormat stopTime;
    private Handler handler;
    private long timeStart;
    private long timeStop;
    private long time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StopclockViewModel stopclockViewModel =
                new ViewModelProvider(this).get(StopclockViewModel.class);

        binding = FragmentStopclockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TextView der Timereit
        stopClockView = root.findViewById(R.id.stop_clock);

        //button zum starten des Timers
        buttonStart = root.findViewById(R.id.start_button);
        stopTime = new SimpleDateFormat("mm:ss.SS", Locale.getDefault());
        buttonStart.setOnClickListener(v -> {
            startTimer();
            //nachdem "starten" gedrückt wurde soll ein aktiver stop button erscheinen
            makeStopButton();
            buttonStop.setEnabled(true);
        });

        //button zum stoppen des Timers
        buttonStop = root.findViewById(R.id.stop_button);
        makeStopButton();

        handler = new Handler(); //handler zum updaten der Uhr

        return root;
    }

    public void startTimer() {
        if (time == 0) {
            //Zeit zum Start des timers wird abgerufen
            timeStart = System.currentTimeMillis();
            timerUp();
        }
        else {
            timeStart = timeStart + (System.currentTimeMillis() - timeStop); //Zeit, die vergangen ist, während der Timer gestoppt war wird nicht eingerechnet
            timerUp();
        }
        //wenn stoppuhr läuft kann sie gestoppt aber nicht gestartet werden
        buttonStop.setEnabled(true);
        buttonStart.setEnabled(false);
    }

    public void stopTimer() { //stoppuhr anhalten
        handler.removeCallbacksAndMessages(null);
        timeStop = System.currentTimeMillis();
        //wenn stoppuhr nicht läuft kann sie gestartet aber nicht gestoppt werden
        buttonStart.setEnabled(true);
        System.out.println(time);
    }

    public void timerUp() { //updated die Zeit der stoppuhr
        //Startzeit des timers wird von aktueller Zeit abgezogen -> Differenz ist die Laufzeit des Timers
        time = System.currentTimeMillis() - timeStart ;
        String clockText = stopTime.format(new Date(time));
        //TextView wird alle 10 ms geupdated
        stopClockView.setText(clockText);
        handler.postDelayed(this::timerUp, 10);
    }

    public void makeResetButton() { //ändert die attribute von buttonStop
        buttonStop.setEnabled(true);
        buttonStop.setText("Reset");
        buttonStop.setOnClickListener(v -> {
            resetStopclock();
            //nach resetten soll wieder stop button erscheinen
            makeStopButton();
        });
    }

    public void makeStopButton() { //verändert die attribute von buttonStop
        buttonStop.setText("Stop");
        buttonStop.setEnabled(false);
        buttonStop.setOnClickListener(v -> {
            stopTimer();
            //nach stoppen soll reset button erscheinen
            makeResetButton();
        });

    }

    public void resetStopclock() {
        time = 0;
        stopClockView.setText("00:00.00");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}