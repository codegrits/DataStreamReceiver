# DataStreamReceiver

DataStreamReceiver is an IntelliJ IDEA plugin that builds on top of
the [real-time data API](https://codegrits.github.io/CodeGRITS/developer/#real-time-data-api)
provided by [CodeGRITS](https://codegrits.github.io/CodeGRITS/). It is designed to receive real-time IDE tracking and
eye tracking data and simply visualize them in two separate windows.

When you install DataStreamReceiver, CodeGRITS will be installed automatically as a dependency.
DataStreamReceiver serves as an example project for future plugin developers and researchers to learn how to use the
real-time data API.

## Build

Find CodeGRITS dependency, i.e., the `build/idea-sandbox/plugins/CodeGRITS` folder in the CodeGRITS project after
running `./gradlew build` command. Then, add it to the `src/main/resources/` folder of DataStreamReceiver project.

Change the python interpreter path in `src/main/java/EyeDataWindow` to your own python interpreter path. Packages
required by CodeGRITS are also required by DataStreamReceiver.

Run `./gradlew build` command in the DataStreamReceiver project to build the plugin, or click `Run` - `Run 'Run Plugin'`
in IntelliJ IDEA to debug the code.

## Contact

If you have any questions, please raise an issue, or contact Junwen An at jan2@nd.edu or Ningzhi Tang at ntang@nd.edu.