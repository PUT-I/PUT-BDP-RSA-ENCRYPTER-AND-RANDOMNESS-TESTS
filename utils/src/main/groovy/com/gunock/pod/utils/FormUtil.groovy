package com.gunock.pod.utils


import javax.swing.*
import javax.swing.border.BevelBorder
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.util.List

class FormUtil {

    static void close(JFrame frame) {
        if (frame != null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        }
    }

    static JPanel createTextFieldWithTitle(String title, String text, boolean editable) {
        JPanel textAreaPanel = new JPanel()
        textAreaPanel.setBorder(new BevelBorder(BevelBorder.LOWERED))
        setBoxLayout(textAreaPanel, BoxLayout.Y_AXIS)

        JLabel textLabel = new JLabel(title)
        textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT)
        textLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT)

        JTextField textArea = new JTextField()
        textArea.setText(text)
        textArea.setEditable(editable)

        JScrollPane scrollPane = new JScrollPane(textArea)

        addAllComponents(textAreaPanel, [textLabel, scrollPane])
        return textAreaPanel
    }

    static JTextField getTextFieldFromPanelWithTitle(JPanel panel) {
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(1)
        JViewport viewport = (JViewport) scrollPane.getComponent(0)
        return (JTextField) viewport.getComponent(0)
    }

    static void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        )
    }

    static void addButton(JComponent component, String text, ActionListener listener) {
        JButton button = new JButton(text)
        button.addActionListener(listener)
        component.add(button)
    }

    static void addButton(JComponent component, String text) {
        addButton(component, text, null)
    }

    static void setBoxLayout(JComponent component, int axis) {
        component.setLayout(new BoxLayout(component, axis))
    }

    static void setBoxLayout(Container container, int axis) {
        setBoxLayout(container as JComponent, axis)
    }

    static void addAllComponents(JComponent parent, List<JComponent> components) {
        for (JComponent component : components) {
            parent.add(component)
        }
    }

    static void addAllComponents(Container parent, List<JComponent> components) {
        addAllComponents(parent as JComponent, components)
    }

    static ActionListener createActionListener(Closure action) {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                action(event)
            }
        }
    }

    static WindowListener createWindowListener(Closure closingAction) {
        return new WindowListener() {
            @Override
            void windowOpened(WindowEvent e) {
            }

            @Override
            void windowClosing(WindowEvent e) {
                closingAction()
            }

            @Override
            void windowClosed(WindowEvent e) {
            }

            @Override
            void windowIconified(WindowEvent e) {
            }

            @Override
            void windowDeiconified(WindowEvent e) {
            }

            @Override
            void windowActivated(WindowEvent e) {
            }

            @Override
            void windowDeactivated(WindowEvent e) {
            }
        }
    }

    static WindowListener onWindowClosingAction(Closure closingAction) {
        return createWindowListener(closingAction)
    }

}
