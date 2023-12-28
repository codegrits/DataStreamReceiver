import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import trackers.EyeTracker;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        EyeTracker eyeTracker = null;
        try {
            String pythonPath = "D:\\ProgramData\\Anaconda3\\python.exe"; // (Ningzhi)
//            String pythonPath = "D:\\Python\\python.exe"; // (Junwen)
            eyeTracker = new EyeTracker(pythonPath, 30.0, true);
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        EyeTracker.setIsRealTimeDataTransmitting(true);
        eyeTracker.setEyeTrackerDataHandler(System.out::println);
        try {
            eyeTracker.startTracking(Objects.requireNonNull(e.getProject()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
