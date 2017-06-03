package com.mauriciotogneri.androidthings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;

public class MainActivity extends Activity
{
    private Handler handler;
    private Gpio ledRed;
    private Gpio ledGreen;
    private Gpio ledBlue;
    private AlphanumericDisplay display;
    private Integer currentLed = 0;
    private Integer currentCharacter = 0;

    private static final String TEXT = "THIS IS A MESSAGE ";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            display = RainbowHat.openDisplay();
            display.setEnabled(true);
            display.display("TEST");

            ledRed = RainbowHat.openLedRed();
            ledRed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            ledGreen = RainbowHat.openLedGreen();
            ledGreen.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            ledBlue = RainbowHat.openLedBlue();
            ledBlue.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            handler = new Handler(Looper.getMainLooper());

            runAndReschedule();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void runAndReschedule()
    {
        try
        {
            display.display(text(TEXT));

            ledRed.setValue(false);
            ledGreen.setValue(false);
            ledBlue.setValue(false);

            if (currentLed == 0)
            {
                ledBlue.setValue(true);
            }
            else if (currentLed == 1)
            {
                ledGreen.setValue(true);
            }
            else if (currentLed == 2)
            {
                ledRed.setValue(true);
            }

            currentLed = (currentLed + 1) % 3;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                runAndReschedule();
            }
        }, 500);
    }

    private String text(String input)
    {
        String result = input.substring(currentCharacter);

        if (currentCharacter > 0)
        {
            result += input.substring(0, currentCharacter - 1);
        }

        currentCharacter = (currentCharacter + 1) % input.length();

        return result.substring(0, 4);
    }
}
