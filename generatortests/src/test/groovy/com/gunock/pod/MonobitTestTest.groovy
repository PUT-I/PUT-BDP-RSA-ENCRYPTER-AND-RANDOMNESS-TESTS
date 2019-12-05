package com.gunock.pod

import spock.lang.Specification
import spock.lang.Unroll

class MonobitTestTest extends Specification {

    @Unroll
    def "#bitString should pass monobit test"() {
        expect:
        FipsTests.monobitTest(bitString)[0]

        where:
        bitString       | _
        "11110000" * 16 | _
        "00001111" * 16 | _
    }

    @Unroll
    def "#bitString should not pass monobit test"() {
        expect:
        !FipsTests.monobitTest(bitString)[0]

        where:
        bitString       | _
        "00000000" * 32 | _
        "11111111" * 32 | _
        "11111000" * 32 | _
        "00000111" * 32 | _
    }

}
