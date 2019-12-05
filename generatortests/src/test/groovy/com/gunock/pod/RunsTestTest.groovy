package com.gunock.pod

import spock.lang.Specification
import spock.lang.Unroll

class RunsTestTest extends Specification {

    @Unroll
    def "#bitString should pass runs test"() {
        expect:
        FipsTests.runsTest(bitString)[0]

        where:
        bitString     | _
        "10011111000" | _
    }

    @Unroll
    def "#bitString should not pass runs test"() {
        expect:
        !FipsTests.runsTest(bitString)[0]

        where:
        bitString    | _
        "10101010"   | _
        "1110001000" | _
    }

}
