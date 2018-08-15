# PointView
类芝麻信用评分控件
## 效果图
<img src="https://github.com/luozhanming/PointView/blob/master/GIF_20180815_200325.gif.F7AD01B5F77704F659E0E82B613ED198.NFC" width="180" height="320" />

## 特性
1.弧形评分控件，可配置控件弧形颜色，文字颜色；<br>
2.可自定义刻度<br>
3.可配置达成子刻度后显示的图案<br>

## 方法
|方法名|描述|
|---|---|
|setRulerColor(int mRulerColor)| 设置弧形颜色|
|setRadius(float mRadius)| 设置弧形半径）|
|setScores(@FloatRange(from = 0, to = 1.0) float... scores)| 设置显示达成图案的子目标|
|setScoreBitmap(int resId)| 设置子目标图案|
|setMaxPoint(int mMaxPoint)| 设置最大值分值|
|setCurrentPoint(int point)| 设置当前分值|
|setTitle(String title)|设置评分标题 |
|setBottomText(String bottomText)| 设置分值下文字 |
|setCurrentPointWithAnimation(final int point, int duration)| 设置当前分值，带动画|

## Attributes（布局文件中使用）
|属性|描述
|---|---
|currentPointTextColor| 当前分值字体颜色
|scaleTextColor| 弧形及其刻度字体颜色
|topAndBottomTextColor| 分值上下方向文案文字颜色
|radius| 弧形半径
|mRulerWidth| 弧形刻度宽度
|pointTextSize| 当前分值字体大小
|currentPoint| 当前分值
|title| 标题
|bottomText| 分值底部文字
|maxPoint | 最大分值
|scoreDrawable | 子目标达成图案

