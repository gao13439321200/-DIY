package com.example.mytest.dto;

import java.util.List;

/**
 * Created by gaopeng on 2016/7/22.
 * 登录Gson实体类
 */
public class LoginInfoGson {

    /**
     * id : 28078
     * auth_id : 42401595
     * auth_password : 111111
     * username : gaopeng1
     * salt : null
     * type : 11
     * pic_name :
     * pic_src :
     * pic : http://192.168.12.227/html/SOURCEP/students/1449804525.jpg
     * nick : 高鹏～
     * signature :
     * province_id : 1
     * city : 161
     * school_id : null
     * grade : null
     * courses : null
     * bookids : null
     * student_name : 高鹏
     * student_phone : 13439321200
     * bangding : 0
     * student_password : null
     * student_sex : 男
     * student_birthday : 2016-03-20
     * student_school : 测试学校
     * student_email : null
     * qq : 1064279058
     * xueduan : 3
     * class_name : null
     * student_info : null
     * student_no : null
     * class_year :
     * parent_name : null
     * parent_phone : 18201259958
     * parent_email : null
     * parent_address : null
     * parent_postcode : null
     * parent_password : null
     * created : 2015-05-17 09:50:31
     * careted_by : null
     * modified : 2016-04-20 16:24:19
     * modified_by : null
     * delete_flag : 0
     * tuijian : 1
     * fendoubi : 3490
     * jingyan : 58685
     * zuanshi : 45
     * level : 18
     * city_id_biaogan : null
     * school_id_biaogan : null
     * verificnumber : null
     * date_from : 2015-05-15
     * date_to : 2018-05-15
     * userpower : null
     * isagree : 1
     * award :
     * level_no : 18
     * pk : {"pkresult1":0,"pkresult2":0,"pknum":0,"pkrank":"0%"}
     * course : ["05","04","03","02","01","07"]
     * skill : [{"id":"1","name":"梦境缠绕","description":"扰乱对方","photo":"http://video.huixueyuan.cn/bagtools/j1.png","type":"1"},{"id":"2","name":"群星坠落","description":"扰乱对方","photo":"http://video.huixueyuan.cn/bagtools/j2.png","type":"1"},{"id":"3","name":"霜之哀伤","description":"扰乱对方","photo":"http://video.huixueyuan.cn/bagtools/j3.png","type":"1"},{"id":"4","name":"冰晶风暴","description":"扰乱对方","photo":"http://video.huixueyuan.cn/bagtools/j4.png","type":"1"}]
     */

    public String success = "1";
    public String message = "";
    private String id;
    private String auth_id;
    private String auth_password;
    private String username;
    private Object salt;
    private String type;
    private String pic_name;
    private String pic_src;
    private String pic;
    private String nick;
    private String signature;
    private String province_id;
    private String city;
    private Object school_id;
    private Object grade;
    private Object courses;
    private Object bookids;
    private String student_name;
    private String student_phone;
    private String bangding;
    private Object student_password;
    private String student_sex;
    private String student_birthday;
    private String student_school;
    private Object student_email;
    private String qq;
    private String xueduan;
    private Object class_name;
    private Object student_info;
    private Object student_no;
    private String class_year;
    private Object parent_name;
    private String parent_phone;
    private Object parent_email;
    private Object parent_address;
    private Object parent_postcode;
    private Object parent_password;
    private String created;
    private Object careted_by;
    private String modified;
    private Object modified_by;
    private String delete_flag;
    private String tuijian;
    private String fendoubi;
    private String jingyan;
    private String zuanshi;
    private String level;
    private Object city_id_biaogan;
    private Object school_id_biaogan;
    private Object verificnumber;
    private String date_from;
    private String date_to;
    private Object userpower;
    private String isagree;
    private String award;
    private String level_no;
    /**
     * pkresult1 : 0
     * pkresult2 : 0
     * pknum : 0
     * pkrank : 0%
     */

    private PkBean pk;
    private List<String> course;
    /**
     * id : 1
     * name : 梦境缠绕
     * description : 扰乱对方
     * photo : http://video.huixueyuan.cn/bagtools/j1.png
     * type : 1
     */

    private List<SkillBean> skill;
    /**
     * user_id : 28078
     * sound_effect : 1
     * music : 0
     * animation : 1
     * power : ["1","2","3","4","5","6","7","8","9"]
     * limitDay : 0
     * defaultIsSet : 0
     */

    private String user_id;
    private String sound_effect;
    private String music;
    private String animation;
    private int limitDay;
    private int defaultIsSet;
    private List<String> power;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public String getAuth_password() {
        return auth_password;
    }

    public void setAuth_password(String auth_password) {
        this.auth_password = auth_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getSalt() {
        return salt;
    }

    public void setSalt(Object salt) {
        this.salt = salt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public String getPic_src() {
        return pic_src;
    }

    public void setPic_src(String pic_src) {
        this.pic_src = pic_src;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getSchool_id() {
        return school_id;
    }

    public void setSchool_id(Object school_id) {
        this.school_id = school_id;
    }

    public Object getGrade() {
        return grade;
    }

    public void setGrade(Object grade) {
        this.grade = grade;
    }

    public Object getCourses() {
        return courses;
    }

    public void setCourses(Object courses) {
        this.courses = courses;
    }

    public Object getBookids() {
        return bookids;
    }

    public void setBookids(Object bookids) {
        this.bookids = bookids;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_phone() {
        return student_phone;
    }

    public void setStudent_phone(String student_phone) {
        this.student_phone = student_phone;
    }

    public String getBangding() {
        return bangding;
    }

    public void setBangding(String bangding) {
        this.bangding = bangding;
    }

    public Object getStudent_password() {
        return student_password;
    }

    public void setStudent_password(Object student_password) {
        this.student_password = student_password;
    }

    public String getStudent_sex() {
        return student_sex;
    }

    public void setStudent_sex(String student_sex) {
        this.student_sex = student_sex;
    }

    public String getStudent_birthday() {
        return student_birthday;
    }

    public void setStudent_birthday(String student_birthday) {
        this.student_birthday = student_birthday;
    }

    public String getStudent_school() {
        return student_school;
    }

    public void setStudent_school(String student_school) {
        this.student_school = student_school;
    }

    public Object getStudent_email() {
        return student_email;
    }

    public void setStudent_email(Object student_email) {
        this.student_email = student_email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getXueduan() {
        return xueduan;
    }

    public void setXueduan(String xueduan) {
        this.xueduan = xueduan;
    }

    public Object getClass_name() {
        return class_name;
    }

    public void setClass_name(Object class_name) {
        this.class_name = class_name;
    }

    public Object getStudent_info() {
        return student_info;
    }

    public void setStudent_info(Object student_info) {
        this.student_info = student_info;
    }

    public Object getStudent_no() {
        return student_no;
    }

    public void setStudent_no(Object student_no) {
        this.student_no = student_no;
    }

    public String getClass_year() {
        return class_year;
    }

    public void setClass_year(String class_year) {
        this.class_year = class_year;
    }

    public Object getParent_name() {
        return parent_name;
    }

    public void setParent_name(Object parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public Object getParent_email() {
        return parent_email;
    }

    public void setParent_email(Object parent_email) {
        this.parent_email = parent_email;
    }

    public Object getParent_address() {
        return parent_address;
    }

    public void setParent_address(Object parent_address) {
        this.parent_address = parent_address;
    }

    public Object getParent_postcode() {
        return parent_postcode;
    }

    public void setParent_postcode(Object parent_postcode) {
        this.parent_postcode = parent_postcode;
    }

    public Object getParent_password() {
        return parent_password;
    }

    public void setParent_password(Object parent_password) {
        this.parent_password = parent_password;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Object getCareted_by() {
        return careted_by;
    }

    public void setCareted_by(Object careted_by) {
        this.careted_by = careted_by;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Object getModified_by() {
        return modified_by;
    }

    public void setModified_by(Object modified_by) {
        this.modified_by = modified_by;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getTuijian() {
        return tuijian;
    }

    public void setTuijian(String tuijian) {
        this.tuijian = tuijian;
    }

    public String getFendoubi() {
        return fendoubi;
    }

    public void setFendoubi(String fendoubi) {
        this.fendoubi = fendoubi;
    }

    public String getJingyan() {
        return jingyan;
    }

    public void setJingyan(String jingyan) {
        this.jingyan = jingyan;
    }

    public String getZuanshi() {
        return zuanshi;
    }

    public void setZuanshi(String zuanshi) {
        this.zuanshi = zuanshi;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Object getCity_id_biaogan() {
        return city_id_biaogan;
    }

    public void setCity_id_biaogan(Object city_id_biaogan) {
        this.city_id_biaogan = city_id_biaogan;
    }

    public Object getSchool_id_biaogan() {
        return school_id_biaogan;
    }

    public void setSchool_id_biaogan(Object school_id_biaogan) {
        this.school_id_biaogan = school_id_biaogan;
    }

    public Object getVerificnumber() {
        return verificnumber;
    }

    public void setVerificnumber(Object verificnumber) {
        this.verificnumber = verificnumber;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public Object getUserpower() {
        return userpower;
    }

    public void setUserpower(Object userpower) {
        this.userpower = userpower;
    }

    public String getIsagree() {
        return isagree;
    }

    public void setIsagree(String isagree) {
        this.isagree = isagree;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getLevel_no() {
        return level_no;
    }

    public void setLevel_no(String level_no) {
        this.level_no = level_no;
    }

    public PkBean getPk() {
        return pk;
    }

    public void setPk(PkBean pk) {
        this.pk = pk;
    }

    public List<String> getCourse() {
        return course;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    public List<SkillBean> getSkill() {
        return skill;
    }

    public void setSkill(List<SkillBean> skill) {
        this.skill = skill;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSound_effect() {
        return sound_effect;
    }

    public void setSound_effect(String sound_effect) {
        this.sound_effect = sound_effect;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public int getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(int limitDay) {
        this.limitDay = limitDay;
    }

    public int getDefaultIsSet() {
        return defaultIsSet;
    }

    public void setDefaultIsSet(int defaultIsSet) {
        this.defaultIsSet = defaultIsSet;
    }

    public List<String> getPower() {
        return power;
    }

    public void setPower(List<String> power) {
        this.power = power;
    }

    public static class PkBean {
        private int pkresult1;
        private int pkresult2;
        private int pknum;
        private String pkrank;

        public int getPkresult1() {
            return pkresult1;
        }

        public void setPkresult1(int pkresult1) {
            this.pkresult1 = pkresult1;
        }

        public int getPkresult2() {
            return pkresult2;
        }

        public void setPkresult2(int pkresult2) {
            this.pkresult2 = pkresult2;
        }

        public int getPknum() {
            return pknum;
        }

        public void setPknum(int pknum) {
            this.pknum = pknum;
        }

        public String getPkrank() {
            return pkrank;
        }

        public void setPkrank(String pkrank) {
            this.pkrank = pkrank;
        }
    }

    public static class SkillBean {
        private String id;
        private String name;
        private String description;
        private String photo;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
