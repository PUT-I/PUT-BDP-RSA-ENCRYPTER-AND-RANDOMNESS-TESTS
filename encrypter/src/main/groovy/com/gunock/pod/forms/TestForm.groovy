package com.gunock.pod.forms

import com.gunock.pod.FipsTests
import com.gunock.pod.utils.FormUtil
import groovy.json.JsonOutput

import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.Dimension
import java.awt.Toolkit


class TestForm extends JFrame {

    final String key

    private JPanel monobitPanel
    private JPanel runsPanel
    private JPanel longRunsPanel
    private JPanel pokerPanel

    TestForm(String key) {
        this.key = key
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - this.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - this.getHeight() / 2)
        this.setLocation(x, y)
        this.setVisible(true)
    }

    void create() {
        List<Object> monobitStatus = FipsTests.monobitTest(key)
        List<Object> runsStatus = FipsTests.runsTest(key)
        List<Object> longRunsStatus = FipsTests.longRunsTest(key)
        List<Object> pokerStatus = FipsTests.pokerTest(key)

        saveResults(monobitStatus, runsStatus, longRunsStatus, pokerStatus)

        monobitPanel = FormUtil.createTextFieldWithTitle('Monobit test', monobitStatus[0].toString(), false)
        runsPanel = FormUtil.createTextFieldWithTitle('Runs test', runsStatus[0].toString(), false)
        longRunsPanel = FormUtil.createTextFieldWithTitle('Long runs test', longRunsStatus[0].toString(), false)
        pokerPanel = FormUtil.createTextFieldWithTitle('Poker', pokerStatus[0].toString(), false)

        this.setTitle('Tests')
        this.getContentPane().add(monobitPanel)
        this.getContentPane().add(runsPanel)
        this.getContentPane().add(longRunsPanel)
        this.getContentPane().add(pokerPanel)
        FormUtil.setBoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        this.setSize(400, 250)
        this.setResizable(false)
    }

    private static void saveResults(List<Object> monobitStatus, List<Object> runsStatus, List<Object> longRunsStatus, List<Object> pokerStatus) {
        Map pokerOccurrencesMap = [:]
        final pokerCombinations = (List<String>) pokerStatus[1]
        final pokerOccurrences = (List<Integer>) pokerStatus[2]
        for (int i = 0; i < pokerCombinations.size(); i++) {
            pokerOccurrencesMap.put(pokerCombinations.get(i), pokerOccurrences.get(i))
        }

        final String results = JsonOutput.toJson(
                [
                        [
                                "Test name" : "Monobit", "Status": monobitStatus[0],
                                "Parameters": ["Number of ones": monobitStatus[1]]
                        ],
                        [
                                "Test name" : "Runs", "Status": runsStatus[0],
                                "Parameters": [
                                        "Runs of ones"       : ["Status": runsStatus[1][0], "Number of runs": runsStatus[1][1]],
                                        "Runs of zeros"      : ["Status": runsStatus[2][0], "Number of runs": runsStatus[2][1]],
                                        "Interval centers"   : runsStatus[3],
                                        "Interval deviations": runsStatus[4]
                                ]
                        ],
                        [
                                "Test name" : "Long runs", "Status": longRunsStatus[0],
                                "Parameters": [
                                        "Long run length": longRunsStatus[1] + "+",
                                ]
                        ],
                        [
                                "Test name" : "Poker", "Status": pokerStatus[0],
                                "Parameters": [
                                        "Occurrences": pokerOccurrencesMap,
                                        "Statistic"  : pokerStatus[3]
                                ]
                        ]
                ]
        )
        new File("last-result.json").write(results)
    }

}
