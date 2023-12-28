import api.RealtimeDataImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.xml.transform.TransformerException;

public class StopAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        RealtimeDataImpl realtimeData = RealtimeDataImpl.getInstance();
        try {
            realtimeData.stopIDETrackerData();
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }
}
