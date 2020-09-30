package com.ekhuilibrary.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ekhui on 2020/6/13.
 */
public class StringUtils {

    /*
     * Created by Ekhui on 2020/8/6.
     * 作用：替换所有空值
     */

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String replaceAll(String text, String regex, String replacement) {
        return text.replaceAll(regex, replacement);
    }

    /*
     * Created by Ekhui on 2020/8/6.
     * 作用：去除特殊字符或将所有中文标号替换为英文标号
     */

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /*
     * Created by Ekhui on 2020/8/6.
     * 作用：半角转换为全角
     */

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {// 全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)// 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /*
     * Created by Ekhui on 2020/8/6.
     * 作用：TextView整齐换行
     */
    public String autoSplitText(final TextView tv, final String indent) {
        final String rawText = tv.getText().toString();//原始文本
        final Paint paint = tv.getPaint();//画笔，还包含字体信息
        int a = tv.getPaddingLeft();
        int b = tv.getPaddingRight();
        int c = tv.getWidth();
        if (c != 0) {
            final float tvWidth = c - a - b;//空间可用宽度

            //将缩进处理成空格
            String indentSpace = "";
            float indentWidth = 0;
            if (!TextUtils.isEmpty(indent)) {
                float rawIndentWidth = paint.measureText(indent);
                if (rawIndentWidth < tvWidth) {
                    while ((indentWidth = paint.measureText(indentSpace)) < rawIndentWidth) {
                        indentSpace += " ";
                    }
                }
            }

            //将原始文本按行拆分
            String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
            StringBuilder sbNewText = new StringBuilder();
            for (String rawTextLine : rawTextLines) {
                if (paint.measureText(rawTextLine) <= tvWidth) {
                    //如果行宽度在空间范围之内，就不处理了
                    sbNewText.append(rawTextLine + "\n");
                } else {
                    //否则按字符测量，在超过可用宽度的前一个字符处，手动替换，加上换行，缩进
                    float lineWidth = 0;
                    for (int i = 0; i != rawTextLine.length(); ++i) {
                        char ch = rawTextLine.charAt(i);
                        //从手动换行的第二行开始加上缩进
                        if (lineWidth < 0.1f && i != 0) {
                            sbNewText.append(indentSpace);
                            lineWidth += indentWidth;
                        }
                        float textWidth = paint.measureText(String.valueOf(ch));
                        lineWidth += textWidth;
                        if (lineWidth < tvWidth) {
                            sbNewText.append(ch);
                        } else {
                            sbNewText.append("\n");
                            lineWidth = 0;
                            --i;
                        }
                    }
                    sbNewText.append("\n");
                }
            }
            //结尾多余的换行去掉
            if (!rawText.endsWith("\n")) {
                sbNewText.deleteCharAt(sbNewText.length() - 1);
            }
            return sbNewText.toString();
        } else {
            return "";
        }
    }
}
