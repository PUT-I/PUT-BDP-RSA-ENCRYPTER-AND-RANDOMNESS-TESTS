package com.gunock.pod

import spock.lang.Specification
import spock.lang.Unroll

class LongRunsTestTest extends Specification {

    @Unroll
    def "#bitString should pass runs test"() {
        expect:
        FipsTests.longRunsTest(bitString)[0]

        where:
        bitString                                                                                                                          | _
        "11001100000101010110110001001100111000000000001001001101010100010001001111010110100000001101011111001100111001101101100010110010" | _
        "11001100000101010110110001001100111000010000001001001101010100010001001111010110100100001101011111001100111001101101100010110010" | _
    }

    @Unroll
    def "#bitString should not pass runs test"() {
        expect:
        !FipsTests.longRunsTest(bitString)[0]

        where:
        bitString                                       | _
        "00000000" * 16                                 | _
        "11111111" * 16                                 | _
        "0000000000000000000000000000000011111111" * 16 | _
        "1111111111111111111111111111111100000000" * 64 | _
    }

}
