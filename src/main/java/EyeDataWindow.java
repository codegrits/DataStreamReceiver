import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import trackers.EyeTracker;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class EyeDataWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        try {
            EyeDataWindowContent eyeDataWindowContent = new EyeDataWindowContent(toolWindow, project);
            Content content = ContentFactory.getInstance().createContent(eyeDataWindowContent.contentPanel, "", false);
            toolWindow.getContentManager().addContent(content);
        } catch (ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class EyeDataWindowContent implements ArrayListChangedListener {
        private JPanel contentPanel = new JPanel();
        private JPanel dataPanel;
        private JScrollPane scrollPane;
        private DataList<JLabel> dataList = new DataList<>();

        public EyeDataWindowContent(ToolWindow toolWindow, Project project) throws ParserConfigurationException, IOException {
            contentPanel.setLayout(new BorderLayout(0, 20));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            createDataPanel();
            JButton startButton = new JButton("Start");
            startButton.addActionListener(e -> {
                try {
                    addData(project);
                } catch (ParserConfigurationException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            contentPanel.add(scrollPane, BorderLayout.NORTH);
            addData(project);
        }

        public void addData(Project project) throws ParserConfigurationException, IOException {
            String pythonPath = "D:\\ProgramData\\Anaconda3\\python.exe"; // (Ningzhi)
//            String pythonPath = "D:\\Python\\python.exe"; // (Junwen)
            EyeTracker eyeTracker = new EyeTracker(pythonPath, 30.0, true);
            EyeTracker.setIsRealTimeDataTransmitting(true);
            eyeTracker.setEyeTrackerDataHandler(element -> {
                Element locationEle = (Element) element.getElementsByTagName("location").item(0);
                Element astEle = (Element) element.getElementsByTagName("ast_structure").item(0);
                String formattedStr = String.format("Timestamp: %s, Line: %s, Column: %s, Token: %s", element.getAttribute("timestamp"), locationEle.getAttribute("line"), locationEle.getAttribute("column"), astEle.getAttribute("token"));
                dataList.addItem(new JLabel(formattedStr));
            });
            dataList.addListener(this);
            eyeTracker.startTracking(project);
        }


        private void createDataPanel() {
            dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            scrollPane = new JBScrollPane(dataPanel);
        }

        @Override
        public void onArrayListChanged() {
            dataPanel.removeAll();
            int size = dataList.size();
            int start = size > 100 ? size - 100 : 0;
            for (int i = start; i < size; i++) {
                dataPanel.add(dataList.get(i));
            }
            dataPanel.revalidate();
            dataPanel.repaint();
        }
    }
}
