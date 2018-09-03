package com.sdxxtop.zhidian.entity;

import com.sdxxtop.zhidian.http.BaseModel;
import com.sdxxtop.zhidian.utils.PartIconColorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：17:14
 * 邮箱：15010104100@163.com
 * 描述：查看部门信息
 */

public class ContactTeacherBean extends BaseModel<ContactTeacherBean.DataEntity> {

    public static class DataEntity implements Serializable {
        private List<PartEntity> part;
        private List<UserinfoEntity> userinfo;
        private List<ParentListBean.DataBeanX.NavBean> nav;

        public List<PartEntity> getPart() {
            return part;
        }

        public void setPart(List<PartEntity> part) {
            this.part = part;
        }

        public List<UserinfoEntity> getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(List<UserinfoEntity> userinfo) {
            this.userinfo = userinfo;
        }

        public List<ParentListBean.DataBeanX.NavBean> getNav() {
            return nav;
        }

        public void setNav(List<ParentListBean.DataBeanX.NavBean> nav) {
            this.nav = nav;
        }

        public List<TeacherBean> getTeacherBean(Set<Integer> partSet, Set<Integer> userSet) {
            List<TeacherBean> teacherBeanList = new ArrayList<>();
            if (part != null) {
                for (int i = 0; i < part.size(); i++) {
                    TeacherBean teacherBean = new TeacherBean();
                    PartEntity partEntity = part.get(i);
                    teacherBean.setType(TeacherBean.PART);
                    int partId = partEntity.getPart_id();
                    teacherBean.setPart_id(partId);
                    teacherBean.setCompany_id(partEntity.getCompany_id());
                    teacherBean.setPart_name(partEntity.getPart_name());
                    teacherBeanList.add(teacherBean);
                    teacherBean.setImg(PartIconColorUtil.getColor(i));
                    if (partSet.contains((Integer) partId)) {
                        teacherBean.setCheck(true);
                    } else {
                        teacherBean.setCheck(false);
                    }
                }
            }

            if (userinfo != null) {
                for (int i = 0; i < userinfo.size(); i++) {
                    TeacherBean teacherBean = new TeacherBean();
                    UserinfoEntity userinfoEntity = userinfo.get(i);
                    teacherBean.setType(TeacherBean.USER);
                    if (i == 0) {
                        //是否是第一个user值
                        teacherBean.setUserFirst(true);
                    } else {
                        teacherBean.setUserFirst(false);
                    }
                    int userid = userinfoEntity.getUserid();
                    teacherBean.setUserid(userid);
                    teacherBean.setPosition(userinfoEntity.getPosition());
                    teacherBean.setImg(userinfoEntity.getImg());
                    teacherBean.setUser_status(userinfoEntity.getUser_status());
                    teacherBean.setName(userinfoEntity.getName());
                    if (userSet.contains((Integer) userid)) {
                        teacherBean.setCheck(true);
                    } else {
                        teacherBean.setCheck(false);
                    }
                    teacherBeanList.add(teacherBean);
                }
            }

            return teacherBeanList;
        }

        public static class PartEntity implements Serializable {
            /**
             * part_id : 18
             * company_id :
             * part_name :
             */

            private int part_id;
            private String company_id;
            private String part_name;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getPart_id() {
                return part_id;
            }

            public void setPart_id(int part_id) {
                this.part_id = part_id;
            }

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }
        }

        public static class UserinfoEntity {
            /**
             * userid : 50000028
             * name :
             * position :
             * img : #aa76ba
             * user_status : 0
             */

            private int userid;
            private String name;
            private String position;
            private String img;
            private int user_status;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getUser_status() {
                return user_status;
            }

            public void setUser_status(int user_status) {
                this.user_status = user_status;
            }
        }

    }
}
