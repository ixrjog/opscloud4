/*
 * Copyright (c) 2016 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.helper;

/**
 * The range class will handle the following situations:
 * <ul>
 * <li>{M,N}: From the M-th element (inclusive) to the N-th element (exclusive).
 * </li>
 * <li>{M,}: From the M-th element (inclusive) to the end.</li>
 * <li>{,N}: From the first element (inclusive) to the N-th element (exclusive).
 * The same as {0,N}.</li>
 * <li>{N}: Just retrieve the N-th element. The same as {N,N+1}.</li>
 * </ul>
 * <p>
 * You can use the {@link Range} class like this:
 *
 * <pre>
 * Range fromAndTo = Range.build().from(1).to(5);
 * Range fromOnly = Range.build().from(3).build();
 * Range toOnly = Range.build().to(5).build();
 * Range only = Range.build().only(3);
 * </pre>
 *
 * @author Karl Heinz Marbaise
 */
public final class Range {

    /**
     * This represents {@code &#123;} (left curly bracket).
     */
    public static final String CURLY_BRACKET_OPEN = "%7B";
    /**
     * This represents {@code &#125;} (right curly bracket).
     */
    public static final String CURLY_BRACKET_CLOSE = "%7D";

    private Integer from;
    private Integer to;

    private Range() {
        this.from = null;
        this.to = null;
    }

    private Range setFrom(int from) {
        if (from < 0) {
            throw new IllegalArgumentException("from value must be greater or equal null.");
        }
        this.from = new Integer(from);
        return this;
    }

    private Range setTo(int to) {
        if (to < 0) {
            throw new IllegalArgumentException("to must be greater or equal null.");
        }
        this.to = new Integer(to);
        return this;
    }

    public String getRangeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CURLY_BRACKET_OPEN);
        if (this.from != null) {
            sb.append(String.format("%d", this.from));
        }

        sb.append(',');

        if (this.to != null) {
            sb.append(String.format("%d", this.to));
        }

        sb.append(CURLY_BRACKET_CLOSE);
        return sb.toString();
    }

    public static final class FromBuilder {
        private Range range;

        public FromBuilder(Range range) {
            this.range = range;
        }

        public Range to(int t) {
            this.range.setTo(t);
            if (range.to <= range.from) {
                throw new IllegalArgumentException("to must be greater than from");
            }
            return this.range;
        }

        public Range build() {
            return this.range;
        }
    }

    public static final class ToBuilder {
        private Range range;

        public ToBuilder(Range range) {
            this.range = range;
        }

        public Range build() {
            return this.range;
        }
    }

    public static final class Builder {
        private Range range;

        protected Builder() {
            this.range = new Range();
        }

        public FromBuilder from(int f) {
            this.range.setFrom(f);
            return new FromBuilder(this.range);
        }

        public ToBuilder to(int t) {
            this.range.setTo(t);
            return new ToBuilder(this.range);
        }

        public Range only(int only) {
            this.range.from = new Integer(only);
            this.range.to = new Integer(only + 1);
            return this.range;
        }
    }

    public static Builder build() {
        return new Builder();
    }
}