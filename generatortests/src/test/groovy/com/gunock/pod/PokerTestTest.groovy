package com.gunock.pod

import spock.lang.Specification
import spock.lang.Unroll

class PokerTestTest extends Specification {

    @Unroll
    def "#bitString should not pass poker test"() {
        expect:
        !FipsTests.pokerTest(bitString)[0]

        where:
        bitString        | _
        "1000" * 5000    | _
        "1001" * 5000  +"0011" * 5000  | _
    }

}
