# EMFRadarSensor v0.1 — source code

Java source for an MIT App Inventor extension. **This archive is not an AIX file.**

Build using a compatible MIT App Inventor extension toolchain (such as Rush or the official extension build environment), with the matching App Inventor runtime and annotation libraries. Toolchain setup and build validation remain outstanding.

## Blocks after building/importing
- `call EMFRadarSensor1.StartSensor`
- `call EMFRadarSensor1.StopSensor`
- `EMFRadarSensor1.SensorAvailable`
- `when EMFRadarSensor1.MagneticFieldChanged x y z total`
- `when EMFRadarSensor1.SensorUnavailable`

All values are in microtesla (µT). The sensor does not provide distance or position of a source. Some phones lack a magnetometer. The sample is not yet compiled or tested on device.
