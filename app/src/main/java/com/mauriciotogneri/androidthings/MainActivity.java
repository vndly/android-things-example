package com.mauriciotogneri.androidthings;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.pio.Gpio;

/**
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 */
public class MainActivity extends Activity
{
    private Handler handler;
    private Gpio ledRed;
    private Gpio ledGreen;
    private Gpio ledBlue;
    private AlphanumericDisplay display;
    private Integer currentLed = 0;

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
}
