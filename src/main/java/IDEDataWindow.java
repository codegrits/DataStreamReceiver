import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import trackers.IDETracker;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;

public class IDEDataWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DataWindowContent dataWindowContent = null;
        try {
            dataWindowContent = new DataWindowContent(toolWindow);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Content content = ContentFactory.getInstance().createContent(dataWindowContent.contentPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private static class DataWindowContent implements ArrayListChangedListener {
        private JPanel contentPanel = new JPanel();
        private JPanel dataPanel;
        private JScrollPane scrollPane;
        private DataList<JLabel> dataList = new DataList<>();

        public DataWindowContent(ToolWindow toolWindow) throws ParserConfigurationException {
            contentPanel.setLayout(new BorderLayout(0, 20));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            createDataPanel();
            contentPanel.add(scrollPane, BorderLayout.NORTH);
            addData();
        }

        private void createDataPanel() {
            dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane = new JBScrollPane(dataPanel);
        }

        private void addData() throws ParserConfigurationException {
            IDETracker ideTracker = IDETracker.getInstance();
            ideTracker.setIsRealTimeDataTransmitting(true);
            ideTracker.setIdeTrackerDataHandler(element -> {
                if (element.getAttribute("id").equals("mouseMoved") || element.getAttribute("id").equals("visibleAreaChanged"))
                    return;
                String formattedStr = "Timestamp: " + element.getAttribute("timestamp") + ", " + "Event: " + element.getAttribute("id");
                JLabel jLabel = new JLabel(formattedStr);
                dataList.addItem(jLabel);
            });
            dataList.addListener(this);
            Project currentProject = ProjectManager.getInstance().getDefaultProject();
            Thread thread = new Thread(() -> {
                ideTracker.startTracking(currentProject);
            });
            thread.start();
        }

        @Override
        public void onArrayListChanged() {
            dataPanel.removeAll();
            for (JLabel jLabel : dataList.getDataList()) {
                dataPanel.add(jLabel);
            }
            dataPanel.revalidate();
            dataPanel.repaint();

        }
    }
}
