/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.comparators.skills;

import java.util.Comparator;

import uk.co.md87.evetool.api.wrappers.data.TrainedSkillInfo;

/**
 * Compares {@link TrainedSkillInfo} by the amount of time required for the
 * next level.
 *
 * @author chris
 */
public class TrainingTimeComparator implements Comparator<TrainedSkillInfo> {

    /** Whether to put complete (untrainable) skills last or not. */
    private final boolean untrainableLast;

    /**
     * Creates a new comparator to sort skills by their training time.
     *
     * @param untrainableLast Whether to put complete (untrainable) skills last or not
     */
    public TrainingTimeComparator(final boolean untrainableLast) {
        this.untrainableLast = untrainableLast;
    }

    /** {@inheritDoc} */
    public int compare(final TrainedSkillInfo o1, final TrainedSkillInfo o2) {
        if (o1.canTrainFurther() && o2.canTrainFurther()) {
            return o1.getTimeToNextLevel() - o2.getTimeToNextLevel();
        } else if (o1.canTrainFurther() && !o2.canTrainFurther()) {
            return untrainableLast ? -1 : 1;
        } else if (!o1.canTrainFurther() && o2.canTrainFurther()) {
            return untrainableLast ? 1 : -1;
        } else {
            return 0;
        }
    }

}
