package com.gunock.pod

import java.util.regex.Pattern

import static java.lang.Math.ceil

// Test definitions sources:
// https://www.researchgate.net/publication/294760536_IP_Core_of_Statistical_Test_Suite_of_FIPS_140-2
// https://www.design-reuse.com/articles/7946/ip-core-of-statistical-test-suite-of-fips-140-2.html#

class FipsTests {
    private static final int BLOCK_SIZE = 20000
    private static final Map<Integer, Integer> intervalCenters = [
            1: BLOCK_SIZE / 8 as int,
            2: BLOCK_SIZE / 16 as int,
            3: BLOCK_SIZE / 32 as int,
            4: BLOCK_SIZE / 64 as int,
            5: BLOCK_SIZE / 128 as int,
            6: BLOCK_SIZE / 128 as int
    ]
    private static final Map<Integer, Integer> deviations = [
            1: ceil(BLOCK_SIZE / 8 * 0.074) as int,
            2: ceil(BLOCK_SIZE / 16 * 0.109) as int,
            3: ceil(BLOCK_SIZE / 32 * 0.157) as int,
            4: ceil(BLOCK_SIZE / 64 * 0.23) as int,
            5: ceil(BLOCK_SIZE / 128 * 0.34) as int,
            6: ceil(BLOCK_SIZE / 128 * 0.34) as int,
    ]

    static List<Object> monobitTest(String bitString) {
        final List<String> blocks = divideIntoBlocks(bitString, BLOCK_SIZE)

        int resultSum = -1
        for (String block in blocks) {
            final int sum = block.count("1")
            resultSum = sum
            if (!(9725 < sum && sum < 10275)) {
                return [false, resultSum]
            }
        }

        return [true, resultSum]
    }

    static List<Object> runsTest(String bitString) {
        final List<String> blocks = divideIntoBlocks(bitString, BLOCK_SIZE)

        final List<Object> onesResult = runsTest(blocks, "1")
        final List<Object> zerosResult = runsTest(blocks, "0")

        return [onesResult[0] && zerosResult[1], onesResult, zerosResult, intervalCenters, deviations]
    }

    static List<Object> runsTest(List<String> blocks, String bitValue) {
        Map<Integer, Integer> counts
        List<Object> result = []
        result.add(true)
        result.add([])
        for (String block in blocks) {
            counts = [1: 0, 2: 0, 3: 0,
                      4: 0, 5: 0, 6: 0]

            String previousBit = ""
            int runLength = 0
            for (int i = 0; i < block.length(); i++) {
                String bit = block[i]
                if (bit != previousBit) {
                    if (runLength != 0) {
                        runLength = runLength > 6 ? 6 : runLength
                        counts.put(runLength, counts.get(runLength) + 1)
                    }
                    runLength = 0
                }
                if (bit == bitValue) {
                    runLength++
                }
                previousBit = bit
            }
            result[1] = counts

            for (int i = 0; i < counts.size(); i++) {
                final int leftBound = intervalCenters.get(i + 1) - deviations.get(i + 1)
                final int rightBound = intervalCenters.get(i + 1) + deviations.get(i + 1)
                if (!(leftBound < counts.get(i + 1) && counts.get(i + 1) < rightBound)) {
                    result[0] = false
                    result.add(counts)
                    return result
                }
            }
        }

        return result
    }

    private static List<String> divideIntoBlocks(String bitString, int BLOCK_SIZE) {
        final int n = bitString.length()
        List<String> blocks = []
        for (int i = 0; i < ceil(n / BLOCK_SIZE); i++) {
            final int substrStart = i * BLOCK_SIZE
            final int substrEnd = (i + 1) * BLOCK_SIZE > n ? n : (i + 1) * BLOCK_SIZE
            blocks.add(bitString.substring(substrStart, substrEnd))
        }
        return blocks
    }

    static List<Object> longRunsTest(String bitString) {
        final int RUN_LENGTH = 26
        final Pattern REGEX = ~("1" * RUN_LENGTH + "+|" + "0" * RUN_LENGTH + "+")
        return [!REGEX.matcher(bitString).find(), RUN_LENGTH]
    }

    static List<Object> pokerTest(String bitString) {
        List<String> combinations = ["0000", "0001", "0010", "0011",
                                     "0100", "0101", "0110", "0111",
                                     "1000", "1001", "1010", "1011",
                                     "1100", "1101", "1110", "1111"]

        List<String> segments = divideIntoBlocks(bitString, 4)

        List<Object> results = []
        results.add(true)
        results.add(combinations)

        final int[] occurences = countOccurences(segments, combinations)

        results.add(occurences)

        double statistic = 0
        for (int occurrence in occurences) {
            statistic += occurrence**2
        }
        statistic *= 16 / 5000
        statistic -= 5000
        results.add(statistic)
        if (!(2.16 < statistic && statistic < 46.17)) {
            results[0] = false
            return results
        }

        return results
    }

    private static int[] countOccurences(List<String> segments, List<String> combinations) {
        int[] occurences = [0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0]
        for (String segment in segments) {
            for (int j = 0; j < combinations.size(); j++) {
                if (segment == combinations[j]) {
                    occurences[j]++
                }
            }
        }
        return occurences
    }

}
