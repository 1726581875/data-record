package yanyu.xmz.recorder.dao.util;

/**
 * @author xiaomingzhang
 * @date 2021/8/19
 */
public class NameConvertUtil {

    /**
     * 转换为数据库规则
     * CourseRecord -> course_record
     * userId -> user_id
     * @param str
     * @return
     */
    public static String toDbRule(String str){
        if(str == null || "".equals(str.trim())){
            return "";
        }
        StringBuilder name = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            //大写字母ASCII码值65~90
            if (chars[i] >= 65 && chars[i] <= 90) {
                if(i != 0) {
                    name.append("_");
                }
                //大小写ASCII码值相差32
                name.append((char) (chars[i] + 32));
            }else {
                name.append(chars[i]);
            }
        }

        return name.toString();
    }

    public static String around(String str, String c){
        return c + str + c;
    }



}
