package com.fwtai.tool;

import com.fwtai.bean.IdCardException;
import com.fwtai.bean.IdentityCard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ToolIdCard{

    // 验证身份证号的正则
    private final static Pattern reg = Pattern.compile("(\\d{17}[0-9a-zA-Z]|\\d{14}[0-9a-zA-Z])");

    /**
     * 通过身份证号获取生日和性别+年龄
     * @param idcard
     * @return
    */
    public static IdentityCard extractIdInfo(final String idcard){
        final String exceptionMsg = Objects.requireNonNull(idcard,"身份证号不能为空");
        if (Objects.equals(idcard,"")) {
            throw new IdCardException(exceptionMsg);
        }
        final Matcher matcher = reg.matcher(idcard);
        if (!matcher.matches()) {
            throw new IdCardException("身份证号码不合法");
        }
        String dateOfBirth = null;
        String gender = null;
        int sex = 3;
        final int length = idcard.length();
        switch (length){
            case 15:
                dateOfBirth  = "19"+idcard.substring(6,8)+idcard.substring(8,10)+idcard.substring(10,12);
                gender = idcard.substring(14,15);
                /*基数为男 偶数为女*/
                if(Integer.parseInt(gender)%2 == 0){
                    sex = 2;
                }else{
                    sex = 1;
                }
                break;
            case 18:
                dateOfBirth = idcard.substring(6,10)+idcard.substring(10,12)+idcard.substring(12,14);
                gender = idcard.substring(16,17);
                /*基数为男 偶数为女*/
                if(Integer.parseInt(gender)%2 == 0){
                    sex = 2;
                }else{
                    sex = 1;
                }
                break;
            default:
                throw new IdCardException("身份证号码不合法");
        }
        final LocalDate birthday = LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(dateOfBirth));
        //获取年龄
        final long age = ChronoUnit.YEARS.between(birthday,LocalDate.now());
        final IdentityCard identityCard = new IdentityCard();
        identityCard.setBirthday(birthday);
        identityCard.setAge(age);
        //获取性别
        switch (sex){
            case 1:
                identityCard.setSex(IdentityCard.Sex.MALE);
                break;
            case 2:
                identityCard.setSex(IdentityCard.Sex.FEMALE);
                break;
            default:
                break;
        }
        return identityCard;
    }
}
