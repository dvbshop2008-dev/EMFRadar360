package com.emfradar.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.OnDestroyListener;
import com.google.appinventor.components.annotations.Extension;

@Extension
@DesignerComponent(
    version = 1,
    description = "Magnetometer for EMF Radar 360. Values in microtesla.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = ""
)
public class EMFRadarSensor extends AndroidNonvisibleComponent implements SensorEventListener, OnDestroyListener {
  private final SensorManager manager;
  private final Sensor magnetometer;
  private boolean running = false;
  private float x, y, z, total;

  public EMFRadarSensor(ComponentContainer container) {
    super(container.$form());
    manager = (SensorManager) container.$context().getSystemService(Context.SENSOR_SERVICE);
    magnetometer = manager == null ? null : manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    container.$form().registerForOnDestroy(this);
  }

  @SimpleProperty(description = "Whether a magnetic-field sensor is present.")
  public boolean SensorAvailable() { return magnetometer != null; }

  @SimpleProperty(description = "Whether measurement is running.")
  public boolean IsRunning() { return running; }

  @SimpleProperty public float X() { return x; }
  @SimpleProperty public float Y() { return y; }
  @SimpleProperty public float Z() { return z; }
  @SimpleProperty public float Total() { return total; }

  @SimpleFunction(description = "Start listening for magnetic-field changes.")
  public void StartSensor() {
    if (magnetometer == null || manager == null) { SensorUnavailable(); return; }
    if (!running) running = manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
  }

  @SimpleFunction(description = "Stop listening and conserve power.")
  public void StopSensor() {
    if (manager != null) manager.unregisterListener(this);
    running = false;
  }

  @SimpleEvent(description = "Magnetic field x/y/z and magnitude, all in microtesla.")
  public void MagneticFieldChanged(float x, float y, float z, float total) {
    EventDispatcher.dispatchEvent(this, "MagneticFieldChanged", x, y, z, total);
  }

  @SimpleEvent public void SensorUnavailable() {
    EventDispatcher.dispatchEvent(this, "SensorUnavailable");
  }

  @Override public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD) return;
    x = event.values[0]; y = event.values[1]; z = event.values[2];
    total = (float) Math.sqrt(x*x + y*y + z*z);
    MagneticFieldChanged(x, y, z, total);
  }
  @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { }
  @Override public void onDestroy() { StopSensor(); }
}
