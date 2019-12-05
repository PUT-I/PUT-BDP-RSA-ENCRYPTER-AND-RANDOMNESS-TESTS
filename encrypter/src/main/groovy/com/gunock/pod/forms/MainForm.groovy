package com.gunock.pod.forms

import com.gunock.pod.cipher.RsaEncrypter
import com.gunock.pod.utils.FormUtil
import com.gunock.pod.utils.HelperUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MainForm extends JFrame {

    String key
    String text

    private JPanel outputFileNamePanel
    private JPanel keyLengthPanel
    private JPanel textLengthPanel
    private JFileChooser textFileChooser
    private JFileChooser keyFileChooser
    private JFrame fileChooserTextFrame
    private JFrame fileChooserKeyFrame

    MainForm() {
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - this.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - this.getHeight() / 2)
        this.setLocation(x, y)
        this.setVisible(true)
    }

    void create() {
        outputFileNamePanel = FormUtil.createTextFieldWithTitle('Output file name', 'file.txt', true)
        keyLengthPanel = FormUtil.createTextFieldWithTitle('Key length', '0 bits', false)
        textLengthPanel = FormUtil.createTextFieldWithTitle('Text length', '0 bits', false)
        final JPanel buttonPanel = createButtonPanel()

        createTextFileChooser()
        createKeyFileChooser()

        this.setTitle('Main Menu')
        this.setDefaultCloseOperation(EXIT_ON_CLOSE)
        this.getContentPane().add(keyLengthPanel)
        this.getContentPane().add(textLengthPanel)
        this.getContentPane().add(outputFileNamePanel)
        this.getContentPane().add(buttonPanel)
        FormUtil.setBoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        this.setSize(450, 250)
        this.setResizable(false)
    }

    JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel()

        FormUtil.addButton(buttonPanel, 'Load key', loadKey())
        FormUtil.addButton(buttonPanel, 'Load file', loadFile())
        FormUtil.addButton(buttonPanel, 'Encrypt/Decrypt', encryptButton())
        FormUtil.addButton(buttonPanel, 'Run tests', testButton())
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT)
        return buttonPanel
    }

    void createTextFileChooser() {
        textFileChooser = new JFileChooser()
        textFileChooser.setCurrentDirectory(new File(System.getProperty('user.dir')))
        textFileChooser.addActionListener(fileChooserTextAction())
        fileChooserTextFrame = new JFrame('Open text file')
        fileChooserTextFrame.getContentPane().add(textFileChooser)
    }

    void createKeyFileChooser() {
        keyFileChooser = new JFileChooser()
        keyFileChooser.setCurrentDirectory(new File(System.getProperty('user.dir')))
        keyFileChooser.addActionListener(fileChooserKeyAction())
        fileChooserKeyFrame = new JFrame('Open key file')
        fileChooserKeyFrame.getContentPane().add(keyFileChooser)
    }

    ActionListener loadKey() {
        return FormUtil.createActionListener { ActionEvent event ->
            fileChooserKeyFrame.setSize(480, 600)
            fileChooserKeyFrame.addWindowListener(FormUtil.onWindowClosingAction({ setVisible(true) }))
            setVisible(false)
            fileChooserKeyFrame.setLocation(getLocation())
            fileChooserKeyFrame.setVisible(true)
        }
    }

    ActionListener loadFile() {
        return FormUtil.createActionListener { ActionEvent event ->
            fileChooserTextFrame.setSize(480, 600)
            fileChooserTextFrame.addWindowListener(FormUtil.onWindowClosingAction({ setVisible(true) }))
            setVisible(false)
            fileChooserTextFrame.setLocation(getLocation())
            fileChooserTextFrame.setVisible(true)
        }
    }

    ActionListener encryptButton() {
        return FormUtil.createActionListener { ActionEvent event ->
            if (text != null && key != null) {
                String result = RsaEncrypter.encryptDecrypt(text, key)
                final String filename = FormUtil.getTextFieldFromPanelWithTitle(outputFileNamePanel).getText()
                HelperUtil.writeFile(filename, result)
                FormUtil.showMessage('Encryption', 'Encryption finished!')
            }
        }
    }

    ActionListener testButton() {
        return FormUtil.createActionListener { ActionEvent event ->
            if (key != null) {
                if (key.length() < 20000) {
                    FormUtil.showMessage("File error", "Key must have 20000 bits or more!")
                } else {
                    new TestForm(key.substring(0, 20000))
                }
            }
        }
    }

    private ActionListener fileChooserTextAction() {
        return FormUtil.createActionListener { ActionEvent event ->
            if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                String fileText = ''
                try {
                    fileText = textFileChooser
                            .getSelectedFile()
                            .getText()
                } catch (FileNotFoundException ignored) {
                    FormUtil.showMessage('Error', 'File could not be opened!')
                }
                text = fileText
                FormUtil.getTextFieldFromPanelWithTitle(textLengthPanel).setText(text.length() * 8 + ' bits')
                FormUtil.close(fileChooserTextFrame)
            }
            else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                FormUtil.close(fileChooserTextFrame)
            }
        }
    }

    private ActionListener fileChooserKeyAction() {
        return FormUtil.createActionListener { ActionEvent event ->
            if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                String fileText = ''
                try {
                    fileText = keyFileChooser
                            .getSelectedFile()
                            .getText()
                } catch (FileNotFoundException ignored) {
                    FormUtil.showMessage('Error', 'File could not be opened!')
                }
                key = fileText
                FormUtil.getTextFieldFromPanelWithTitle(keyLengthPanel).setText(key.length() + ' bits')
                FormUtil.close(fileChooserKeyFrame)
            }
            else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                FormUtil.close(fileChooserKeyFrame)
            }
        }
    }

}
