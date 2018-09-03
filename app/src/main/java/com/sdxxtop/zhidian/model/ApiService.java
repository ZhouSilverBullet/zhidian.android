package com.sdxxtop.zhidian.model;

import com.sdxxtop.zhidian.entity.AppInitBean;
import com.sdxxtop.zhidian.entity.ApplyIndexBean;
import com.sdxxtop.zhidian.entity.ApplyListBean;
import com.sdxxtop.zhidian.entity.ApplyMonthBean;
import com.sdxxtop.zhidian.entity.ApproverIndexBean;
import com.sdxxtop.zhidian.entity.AttendanceAverageBean;
import com.sdxxtop.zhidian.entity.AttendanceDetailBean;
import com.sdxxtop.zhidian.entity.AttendanceStatisticalBean;
import com.sdxxtop.zhidian.entity.AttendanceWatchBean;
import com.sdxxtop.zhidian.entity.CheckShortCodeBean;
import com.sdxxtop.zhidian.entity.ClassNameBean;
import com.sdxxtop.zhidian.entity.CompanyModifyInfoBean;
import com.sdxxtop.zhidian.entity.CompanyShowInfoBean;
import com.sdxxtop.zhidian.entity.ContactCollectBean;
import com.sdxxtop.zhidian.entity.ContactIndexBean;
import com.sdxxtop.zhidian.entity.ContactPartBean;
import com.sdxxtop.zhidian.entity.ContactRemoveBean;
import com.sdxxtop.zhidian.entity.ContactTeacherBean;
import com.sdxxtop.zhidian.entity.ContactUserInfoBean;
import com.sdxxtop.zhidian.entity.CourseIndexBean;
import com.sdxxtop.zhidian.entity.CreateCompanyBean;
import com.sdxxtop.zhidian.entity.DeviceAddBean;
import com.sdxxtop.zhidian.entity.DeviceIndexBean;
import com.sdxxtop.zhidian.entity.DeviceModifyBean;
import com.sdxxtop.zhidian.entity.DeviceModifyInfoBean;
import com.sdxxtop.zhidian.entity.HomeworkDetailBean;
import com.sdxxtop.zhidian.entity.HomeworkListBean;
import com.sdxxtop.zhidian.entity.JoinByCodeBean;
import com.sdxxtop.zhidian.entity.MainIndexBean;
import com.sdxxtop.zhidian.entity.MainWeekBean;
import com.sdxxtop.zhidian.entity.ModPasswordBean;
import com.sdxxtop.zhidian.entity.ModPwdByFormerBean;
import com.sdxxtop.zhidian.entity.MsgApprovalBean;
import com.sdxxtop.zhidian.entity.MsgIndexBean;
import com.sdxxtop.zhidian.entity.MsgNoticeBean;
import com.sdxxtop.zhidian.entity.NormalLoginBean;
import com.sdxxtop.zhidian.entity.NoticeAddBean;
import com.sdxxtop.zhidian.entity.NoticeDataBean;
import com.sdxxtop.zhidian.entity.NoticeReadBean;
import com.sdxxtop.zhidian.entity.NoticeShow2PeopleBean;
import com.sdxxtop.zhidian.entity.NoticeShowPeopleBean;
import com.sdxxtop.zhidian.entity.ParentListBean;
import com.sdxxtop.zhidian.entity.ReadClassBean;
import com.sdxxtop.zhidian.entity.RegisterRegBean;
import com.sdxxtop.zhidian.entity.ReportIndexBean;
import com.sdxxtop.zhidian.entity.ReportReadBean;
import com.sdxxtop.zhidian.entity.RightPartBean;
import com.sdxxtop.zhidian.entity.SchedulingManageBean;
import com.sdxxtop.zhidian.entity.SelectionDateBean;
import com.sdxxtop.zhidian.entity.SendCodeBean;
import com.sdxxtop.zhidian.entity.ShowClassBean;
import com.sdxxtop.zhidian.entity.ShowCompanyBean;
import com.sdxxtop.zhidian.entity.StatMonthBean;
import com.sdxxtop.zhidian.entity.StatPartSignBean;
import com.sdxxtop.zhidian.entity.StatPeopleBean;
import com.sdxxtop.zhidian.entity.StateIndexBean;
import com.sdxxtop.zhidian.entity.StudentAttendanceBean;
import com.sdxxtop.zhidian.entity.StudentClassBean;
import com.sdxxtop.zhidian.entity.SubmissionBean;
import com.sdxxtop.zhidian.entity.UcenterIndexBean;
import com.sdxxtop.zhidian.entity.UcenterModMobileBean;
import com.sdxxtop.zhidian.entity.UcenterNoticeIndexBean;
import com.sdxxtop.zhidian.entity.UcenterNoticeModifyBean;
import com.sdxxtop.zhidian.entity.UcenterOutIndexBean;
import com.sdxxtop.zhidian.entity.UcenterOutModifyBean;
import com.sdxxtop.zhidian.entity.UcenterRemindBean;
import com.sdxxtop.zhidian.entity.UcenterSendCodeBean;
import com.sdxxtop.zhidian.entity.UcenterSetBean;
import com.sdxxtop.zhidian.entity.UcenterUserinfoBean;
import com.sdxxtop.zhidian.entity.UserCourseBean;
import com.sdxxtop.zhidian.entity.VerfiyCodeBean;
import com.sdxxtop.zhidian.entity.VoteIndexBean;
import com.sdxxtop.zhidian.entity.VoteReadBean;
import com.sdxxtop.zhidian.entity.VoteShowPeopleBean;
import com.sdxxtop.zhidian.entity.WorkListBean;
import com.sdxxtop.zhidian.http.BaseModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * 作者：CaiCM
 * 日期：2018/3/22  时间：13:24
 * 邮箱：15010104100@163.com
 * 描述：存储接口地址
 *
 * @FormUrlEncoded 允许表单转码
 * (解决乱码的问题)
 */

public interface ApiService {

    /**
     * APP_SDK初始化
     */
    @FormUrlEncoded
    @POST("app/init")
    Call<AppInitBean> postAppInit(@Field("data") String data);


    //TODO  /*=============================↓首頁模块=============================*/

    /**
     * 首页
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("main/index")
    Call<MainIndexBean> postMainIndex(@Field("data") String data);

    /**
     * 首页打卡
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("main/sign")
    Call<BaseModel> postMainSign(@Field("data") String data);

    /**
     * 外勤上传图片
     *
     * @param map
     * @return
     */
    @Multipart
    @POST("main/img")
    Call<BaseModel> postMainImg(@PartMap HashMap<String, RequestBody> map);

    /**
     * 外勤删除图片
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("main/delImg")
    Call<ResponseBody> postMainDelImg(@Field("data") String data);

    /**
     * 查看指定日期打卡
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("main/day")
    Call<MainIndexBean> postMainDay(@Field("data") String data);

    /**
     * 查看指定日期打卡
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("main/week")
    Call<MainWeekBean> postMainWeek(@Field("data") String data);

    //TODO  /*=============================↑首頁模块=============================*/

    //TODO  /*=============================↓申请模块=============================*/

    /**
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("apply/index")
    Call<ApplyIndexBean> postApplyIndex(@Field("data") String data);


    @FormUrlEncoded
    @POST("apply/{name}")
    Call<ApplyListBean> postApplyList(@Path("name") String name, @Field("data") String data);

    @FormUrlEncoded
    @POST("apply/{name}")
    Call<BaseModel> postApplyLeave(@Path("name") String name, @Field("data") String data);

    @Multipart
    @POST("apply/{name}")
    Call<BaseModel> postApplyLeave(@Path("name") String name, @PartMap HashMap<String, RequestBody> data);

    @FormUrlEncoded
    @POST("apply/{name}")
    Call<ApproverIndexBean> postApplyApprover(@Path("name") String name, @Field("data") String data);

    @FormUrlEncoded
    @POST("apply/day")
    Call<SelectionDateBean> postApplyMainDay(@Field("data") String data);

    @FormUrlEncoded
    @POST("apply/month")
    Call<ApplyMonthBean> postApplyMonth(@Field("data") String data);

    @FormUrlEncoded
    @POST("apply/getClassName")
    Call<ClassNameBean> postClassName(@Field("data") String data);

    /**
     * read
     * readParent
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("apply/{name}")
    Call<SubmissionBean> postApplyRead(@Path("name") String name, @Field("data") String data);

    /**
     * modify
     * modifyParent
     *
     * @param name
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("apply/{name}")
    Call<BaseModel> postApplyModify(@Path("name") String name, @Field("data") String data);

    @FormUrlEncoded
    @POST("apply/batch")
    Call<BaseModel> postApplyBatch(@Field("data") String data);

    //TODO  /*=============================↑申请模块=============================*/
    //TODO  /*=============================↓消息中心=============================*/
    @FormUrlEncoded
    @POST("msg/index")
    Call<MsgIndexBean> postMsgIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("msg/notice")
    Call<MsgNoticeBean> postMsgNotice(@Field("data") String data);

    @FormUrlEncoded
    @POST("msg/apply")
    Call<MsgApprovalBean> postMsgApply(@Field("data") String data);

    //TODO  /*=============================↑消息中心=============================*/


    //TODO  /*=============================↓我的考勤=============================*/

    @FormUrlEncoded
    @POST("user/index")
    Call<AttendanceWatchBean> postUserIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/stat")
    Call<AttendanceStatisticalBean> postUserStat(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/average")
    Call<AttendanceAverageBean> postUserAverager(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/detail")
    Call<AttendanceDetailBean> postUserDetail(@Field("data") String data);


    //TODO  /*=============================↑我的考勤=============================*/


    //TODO  /*=============================↓我的外勤=============================*/

    //我的外勤
    @FormUrlEncoded
    @POST("user/outIndex")
    Call<AttendanceWatchBean> postUserOutIndex(@Field("data") String data);

    //TODO  /*=============================↑我的外勤=============================*/

    //TODO  /*=============================↓排班=============================*/

    //排班
    @FormUrlEncoded
    @POST("classes/index")
    Call<SchedulingManageBean> postClassIndex(@Field("data") String data);

    //删除排班
    @FormUrlEncoded
    @POST("classes/del")
    Call<BaseModel> postClassDel(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/add")
    Call<BaseModel> postClassAdd(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/addClass")
    Call<BaseModel> postClassAddClass(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/modifyClass")
    Call<BaseModel> postClassModifyClass(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/showClass")
    Call<ShowClassBean> postShowClass(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/checkRepeat")
    Call<BaseModel> postCheckRepeat(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/readClass")
    Call<ReadClassBean> postReadClass(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/getClassesUser")
    Call<ContactPartBean> postGetClassesUser(@Field("data") String data);

    //排班
    @FormUrlEncoded
    @POST("classes/modify")
    Call<BaseModel> postClassesModify(@Field("data") String data);

    //TODO  /*=============================↑排班=============================*/


    //TODO  /*=============================↓统计=============================*/

    @FormUrlEncoded
    @POST("stat/index")
    Call<StateIndexBean> postStatIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/partSign")
    Call<StatPartSignBean> postStatPartSign(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/partApply")
    Call<StatPartSignBean> postStatPartApply(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/people")
    Call<StatPeopleBean> postStatPeople(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/userSign")
    Call<StatMonthBean> postStatUserSign(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/userApply")
    Call<StatMonthBean> postStatUserApply(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/getPart")
    Call<RightPartBean> postStatGetPart(@Field("data") String data);

    /**
     * 比较特殊后面添加的
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST("stat/getPart")
    Call<UcenterOutIndexBean> postOutGetPart(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/modify")
    Call<BaseModel> postStatModify(@Field("data") String data);

    @FormUrlEncoded
    @POST("stat/show")
    Call<StateIndexBean> postStatShow(@Field("data") String data);

    //TODO  /*=============================↑统计=============================*/

    //TODO  /*=============================↓人脸识别=============================*/

    @Multipart
    @POST("face/verify")
    Call<BaseModel> postFaceVerify(@PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST("face/reg")
    Call<BaseModel> postFaceReg(@PartMap HashMap<String, RequestBody> map);

    //TODO  /*=============================↑人脸识别=============================*/

    //TODO  /*=============================↓注册模块=============================*/

    /**
     * 发送验证码
     */
    @FormUrlEncoded
    @POST("register/sendCode")
    Call<SendCodeBean> postSendCode(@Field("data") String data);

    /**
     * 校验验证码（同忘记密码）
     */
    @FormUrlEncoded
    @POST("register/verfiyCode")
    Call<VerfiyCodeBean> postVerfiyCode(@Field("data") String data);

    /**
     * 提交注册
     */
    @FormUrlEncoded
    @POST("register/reg")
    Call<RegisterRegBean> postRegisterReg(@Field("data") String data);

    //TODO  /*=============================↓登陆模块=============================*/

    /**
     * 普通登录
     */
    @FormUrlEncoded
    @POST("login/normalLogin")
    Call<NormalLoginBean> postNormalLogin(@Field("data") String data);

    /**
     * 自动登录
     */
    @FormUrlEncoded
    @POST("login/autoLogin")
    Call<NormalLoginBean> postAutoLogin(@Field("data") String data);

    //TODO  /*=============================↓忘记密码模块=============================*/

    /**
     * 发送验证码
     */

    @FormUrlEncoded
    @POST("password/sendCode")
    Call<SendCodeBean> postPasswordSeendCode(@Field("data") String data);
    /**
     * 校验验证码（同注册接口 postVerfiyCode）
     */
    /**
     * 校验验证码（同忘记密码）
     */
    @FormUrlEncoded
    @POST("password/verfiyCode")
    Call<VerfiyCodeBean> postPasswordVerfiyCode(@Field("data") String data);

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("password/modPassword")
    Call<ModPasswordBean> postModPassword(@Field("data") String data);

    //TODO  /*=============================↓公司模块=============================*/

    /**
     * 创建公司
     */
    @FormUrlEncoded
    @POST("company/createCompany")
    Call<CreateCompanyBean> postCreateCompany(@Field("data") String data);

    /**
     * 校验公司短码
     */
    @FormUrlEncoded
    @POST("company/checkShortCode")
    Call<CheckShortCodeBean> postCheckShortCode(@Field("data") String data);

    /**
     * 短码加入公司
     */
    @FormUrlEncoded
    @POST("company/joinByCode")
    Call<JoinByCodeBean> postJoinByCode(@Field("data") String data);

    /**
     * 手机号添加员工
     */
    @FormUrlEncoded
    @POST("company/joinByMobile")
    Call<BaseModel> postJoinByMobile(@Field("data") String data);

    /**
     * 校验验证码（同忘记密码）
     */
    @FormUrlEncoded
    @POST("company/transfer")
    Call<BaseModel> postCompanyTransfer(@Field("data") String data);

    //TODO  /*=============================↓通讯录模块=============================*/

    /**
     * 通讯录主页
     */
    @FormUrlEncoded
    @POST("contact/index")
    Call<ContactIndexBean> postContactIndex(@Field("data") String data);

    /**
     * 通讯录搜索
     */
    @FormUrlEncoded
    @POST("contact/search")
    Call<ContactIndexBean> postContactSearch(@Field("data") String data);

    /**
     * 组织架构
     */
    @FormUrlEncoded
    @POST("contact/organize")
    Call<ContactPartBean> postContactOrganize(@Field("data") String data);

    /**
     * 查看部门信息
     */
    @FormUrlEncoded
    @POST("contact/part")
    Call<ContactPartBean> postContactPart(@Field("data") String data);

    /**
     * 查看部门信息
     * part
     * organize
     */
    @FormUrlEncoded
    @POST("contact/{name}")
    Call<ContactTeacherBean> postContactTeacher(@Path("name") String name, @Field("data") String data);

    /**
     * 查看个人信息
     */
    @FormUrlEncoded
    @POST("contact/{name}")
    Call<ContactUserInfoBean> postContactUserInfo(@Path("name") String name, @Field("data") String data);

    /**
     * 收藏和取消收藏用户
     */
    @FormUrlEncoded
    @POST("contact/collect")
    Call<ContactCollectBean> postContactCollect(@Field("data") String data);

    /**
     * 解绑用户设备
     */
    @FormUrlEncoded
    @POST("contact/remove")
    Call<ContactRemoveBean> postContactRemove(@Field("data") String data);

    /**
     * 查看个人信息
     */
    @FormUrlEncoded
    @POST("contact/parentInfo")
    Call<BaseModel> postContactParentInfo(@Field("data") String data);

    /**
     * 家长
     */
    @FormUrlEncoded
    @POST("contact/parent")
    Call<ParentListBean> postContactParent(@Field("data") String data);


    //TODO  /*=============================↓公告模块=============================*/

    /**
     * 公告主页
     */
    @FormUrlEncoded
    @POST("notice/index")
    Call<NoticeDataBean> postNoticeIndex(@Field("data") String data);

    /**
     * 添加公告
     */
//    @FormUrlEncoded
//    @POST("notice/add")
//    Call<NoticeAddBean> postNoticeAdd(@Field("data") String data);
    @Multipart
    @POST("notice/add")
    Call<NoticeAddBean> postNoticeAdd(@PartMap HashMap<String, RequestBody> map);

    /**
     * 浏览公告
     */
    @FormUrlEncoded
    @POST("notice/read")
    Call<NoticeReadBean> postNoticeRead(@Field("data") String data);

    /**
     * 公告阅读人详情
     */
    @FormUrlEncoded
    @POST("notice/showPeople")
    Call<NoticeShowPeopleBean> postShowNoticePeople(@Field("data") String data);

    /**
     * 公告阅读人详情
     */
    @FormUrlEncoded
    @POST("notice/showPeople")
    Call<NoticeShow2PeopleBean> postNoticeShowPeople(@Field("data") String data);

    /**
     * 撤回公告
     */
    @FormUrlEncoded
    @POST("notice/revoke")
    Call<BaseModel> postRevokeNotice(@Field("data") String data);

    //TODO  /*=============================↓民主评议模块=============================*/

    /**
     * 民主评议主页
     */
    @FormUrlEncoded
    @POST("vote/index")
    Call<VoteIndexBean> postVoteIndex(@Field("data") String data);

    /**
     * 添加评分
     */
    @Multipart
    @POST("vote/addMark")
    Call<BaseModel> postVoteAddMark(@PartMap HashMap<String, RequestBody> map);

    /**
     * 添加投票
     */
    @Multipart
    @POST("vote/addVote")
    Call<BaseModel> postVoteAddVote(@PartMap HashMap<String, RequestBody> map);

    /**
     * 浏览民主评议
     */
    @FormUrlEncoded
    @POST("vote/read")
    Call<VoteReadBean> postVoteRead(@Field("data") String data);

    /**
     * 民主评议(打分/投票)
     */
    @FormUrlEncoded
    @POST("vote/mark")
    Call<BaseModel> postVoteMark(@Field("data") String data);

    /**
     * 显示参与人详情
     */
    @FormUrlEncoded
    @POST("vote/showPeople")
    Call<VoteShowPeopleBean> postVoteShowPeople(@Field("data") String data);

    /**
     * 修改/删除民主评议
     */
    @FormUrlEncoded
    @POST("vote/modfiy")
    Call<BaseModel> postVoteModfiy(@Field("data") String data);

    //TODO  /*=============================↓我的模块=============================*/

    /**
     * 我的模块主页
     */
    @FormUrlEncoded
    @POST("ucenter/index")
    Call<UcenterIndexBean> postUcenterIndex(@Field("data") String data);

    /**
     * 账号信息
     */
    @FormUrlEncoded
    @POST("ucenter/userinfo")
    Call<UcenterUserinfoBean> postUcenterUserinfo(@Field("data") String data);

    /**
     * 修改用户头像（上传）
     */
    @Multipart
    @POST("ucenter/modImg")
    Call<BaseModel> postUcenterModImg(@PartMap HashMap<String, RequestBody> map);

    /**
     * 我的设置主页
     */
    @FormUrlEncoded
    @POST("ucenter/set")
    Call<UcenterSetBean> postUcenterSet(@Field("data") String data);

    /**
     * 设备管理主页
     */
    @FormUrlEncoded
    @POST("device/index")
    Call<DeviceIndexBean> postDeviceIndex(@Field("data") String data);

    /**
     * 新增考勤设备
     */
    @FormUrlEncoded
    @POST("device/add")
    Call<DeviceAddBean> postDeviceAdd(@Field("data") String data);

    /**
     * 修改考勤设备状态
     */
    @FormUrlEncoded
    @POST("device/modify")
    Call<DeviceModifyBean> postDeviceModify(@Field("data") String data);

    /**
     * 修改考勤设备信息
     */
    @FormUrlEncoded
    @POST("device/modifyInfo")
    Call<DeviceModifyInfoBean> postDeviceModifyInfo(@Field("data") String data);

    /**
     * 外勤权限主页
     */
    @FormUrlEncoded
    @POST("ucenter/outIndex")
    Call<UcenterOutIndexBean> postUcenterOutIndex(@Field("data") String data);

    /**
     * 修改外勤权限
     */
    @FormUrlEncoded
    @POST("ucenter/outModify")
    Call<UcenterOutModifyBean> postUcenterOutModify(@Field("data") String data);

    /**
     * 修改打卡提醒
     */
    @FormUrlEncoded
    @POST("ucenter/remind")
    Call<UcenterRemindBean> postUcenterRemind(@Field("data") String data);

    /**
     * 信息通知主页
     */
    @FormUrlEncoded
    @POST("ucenter/noticeIndex")
    Call<UcenterNoticeIndexBean> postUcenterNoticeIndex(@Field("data") String data);

    /**
     * 修改信息通知
     */
    @FormUrlEncoded
    @POST("ucenter/noticeModify")
    Call<UcenterNoticeModifyBean> postUcenterNoticeModify(@Field("data") String data);

    /**
     * 修改手机号发送验证码
     */
    @FormUrlEncoded
    @POST("ucenter/sendCode")
    Call<UcenterSendCodeBean> postUcenterSendCode(@Field("data") String data);

    /**
     * 修改手机号
     */
    @FormUrlEncoded
    @POST("ucenter/modMobile")
    Call<UcenterModMobileBean> postUcenterModMobile(@Field("data") String data);

    /**
     * 通过原密码修改密码
     */
    @FormUrlEncoded
    @POST("password/modPwdByFormer")
    Call<ModPwdByFormerBean> postModPwdByFormer(@Field("data") String data);

    /**
     * 用户加入的公司列表
     */
    @FormUrlEncoded
    @POST("company/showCompany")
    Call<ShowCompanyBean> postShowCompany(@Field("data") String data);

    /**
     * 公司信息展示
     */
    @FormUrlEncoded
    @POST("company/showInfo")
    Call<CompanyShowInfoBean> postCompanyShowInfo(@Field("data") String data);

    /**
     * 修改公司信息
     */
    @FormUrlEncoded
    @POST("company/modifyInfo")
    Call<CompanyModifyInfoBean> postCompanyModifyInfo(@Field("data") String data);


    //TODO  /*=============================↓工作汇报=============================*/

    @FormUrlEncoded
    @POST("report/index")
    Call<ReportIndexBean> postReportIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("report/add")
    Call<BaseModel> postReportAdd(@Field("data") String data);

    @FormUrlEncoded
    @POST("report/read")
    Call<ReportReadBean> postReportRead(@Field("data") String data);

    @FormUrlEncoded
    @POST("report/comment")
    Call<BaseModel> postReportComment(@Field("data") String data);

    @FormUrlEncoded
    @POST("msg/report")
    Call<WorkListBean> postMsgReport(@Field("data") String data);

    //TODO  /*=============================↑工作汇报=============================*/
    //TODO  /*=============================↓布置作业=============================*/

    @Multipart
    @POST("user/addTask")
    Call<BaseModel> postUserAddTask(@PartMap HashMap<String, RequestBody> map);

    @FormUrlEncoded
    @POST("user/taskIndex")
    Call<HomeworkListBean> postUserTaskIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/readTask")
    Call<HomeworkDetailBean> postUserReadTask(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/revokeTask")
    Call<BaseModel> postUserRevokeTask(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/course")
    Call<UserCourseBean> postUserCourse(@Field("data") String data);

    //TODO  /*=============================↑布置作业=============================*/

    //TODO  /*=============================↓课程表=============================*/

    @FormUrlEncoded
    @POST("timetable/index")
    Call<CourseIndexBean> postTimeTableIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("timetable/add")
    Call<BaseModel> postTimeTableAdd(@Field("data") String data);

    @FormUrlEncoded
    @POST("timetable/checkRepeat")
    Call<BaseModel> postTimeTableCheckRepeat(@Field("data") String data);

    //TODO  /*=============================↑课程表=============================*/

    //TODO  /*=============================↓学生考勤=============================*/

    @FormUrlEncoded
    @POST("student/index")
    Call<StudentAttendanceBean> postStudentIndex(@Field("data") String data);

    @FormUrlEncoded
    @POST("student/modify")
    Call<BaseModel> postStudentModify(@Field("data") String data);

    @FormUrlEncoded
    @POST("student/modifyClass")
    Call<BaseModel> postStudentModifyClass(@Field("data") String data);

    @FormUrlEncoded
    @POST("student/showClass")
    Call<StudentClassBean> postStudentShowClass(@Field("data") String data);

    //TODO  /*=============================↑学生考勤=============================*/

    //TODO  /*=============================↓腾讯im=============================*/

    /**
     * @param data name: createGroup  addGroupMember
     * @return
     */
    @FormUrlEncoded
    @POST("tim/{name}")
    Call<BaseModel> postTimGroup(@Path("name") String name, @Field("data") String data);

    @FormUrlEncoded
    @POST("tim/getSign")
    Call<BaseModel> postTimGetSign(@Field("data") String data);

    @Multipart
    @POST("tim/modifyUserImg")
    Call<BaseModel> postTimModifyUserImg(@PartMap HashMap<String, RequestBody> map);

    @FormUrlEncoded
    @POST("tim/modifyGroupImg")
    Call<BaseModel> postTimModifyGroupImg(@PartMap HashMap<String, RequestBody> map);

    @FormUrlEncoded
    @POST("tim/addFriend")
    Call<BaseModel> postTimAddFriend(@Field("data") String data);

    //TODO  /*=============================↑腾讯im=============================*/
}