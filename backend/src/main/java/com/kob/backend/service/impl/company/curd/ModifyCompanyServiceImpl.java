package com.kob.backend.service.impl.company.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.*;
import com.kob.backend.pojo.*;
import com.kob.backend.service.company.curd.ModifyCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ModifyCompanyServiceImpl implements ModifyCompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> modifycompany(Integer id, String companyname, String photo, String ownername, String telephone, String note) {
        Map<String , String> map = new HashMap<>();
        if (companyname == null) {
            map.put("error_message", "企业名称不能为空");
            return map;
        }

        if (ownername == null) {
            map.put("error_message", "联系人不能为空");
            return map;
        }

        if (telephone == null) {
            map.put("error_message", "电话不能为空");
            return map;
        }

        if (!isMobile(telephone)) {
            map.put("error_message", "电话格式错误");
            return map;
        }

        if (companyname.length() > 100) {
            map.put("error_message", "企业名称长度不能大于100");
            return map;
        }
        QueryWrapper<Company> querywrapper_id = new QueryWrapper<>();
        querywrapper_id.eq("id" , id);
        Company company = companyMapper.selectOne(querywrapper_id);
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyname", companyname);
        List<Company> companies = companyMapper.selectList(queryWrapper);
        if (!companies.isEmpty() && !companyname.equals(company.getCompanyname())) {
            map.put("error_message", "企业名称已存在");
            return map;
        }

        Company new_company = new Company(id , companyname , company.getPassword() , photo , ownername , telephone , company.getAdminname(), company.getSymbol() , note);
        companyMapper.updateById(new_company);

        QueryWrapper<Organization> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("organizationname" , company.getCompanyname());
        Organization organization = organizationMapper.selectOne(queryWrapper2);
        Organization new_organization = new Organization(organization.getId() , organization.getUporganization() , companyname , organization.getCharger() , organization.getTelephone() , organization.getEmail() , organization.getStatus() , organization.getCreattime());
        organizationMapper.updateById(new_organization);

        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.eq("tenant" , company.getCompanyname());
        List<News> news_list = newsMapper.selectList(newsQueryWrapper);
        for(News news : news_list){
            News new_news = new News(news.getNewsid() , news.getTitle(), news.getSummary() , news.getImagePath() , news.getContent() , news.getAuthor() , companyname);
            newsMapper.updateById(new_news);
        }

        QueryWrapper<Meeting> meetingQueryWrapper = new QueryWrapper<>();
        meetingQueryWrapper.eq("companyname" , company.getCompanyname());
        List<Meeting> meeting_list = meetingMapper.selectList(meetingQueryWrapper);
        for(Meeting meeting : meeting_list)
        {
            Meeting new_meeting = new Meeting(meeting.getMeetingid() , meeting.getMeetingname() , meeting.getCreator() , meeting.getContent() , meeting.getStarttime() , meeting.getEndtime() , meeting.getPhoto() , companyname);
            meetingMapper.updateById(new_meeting);
        }

        QueryWrapper<Lesson> lessonQueryWrapper = new QueryWrapper<>();
        lessonQueryWrapper.eq("companyname" , company.getCompanyname());
        List<Lesson> lesson_list = lessonMapper.selectList(lessonQueryWrapper);
        for(Lesson lesson : lesson_list)
        {
            Lesson new_lesson = new Lesson(lesson.getId() , lesson.getLessonname() , lesson.getLessonintro() , lesson.getLessonauthor() , lesson.getPhoto() , lesson.getVideo() , companyname);
            lessonMapper.updateById(new_lesson);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("companyname" , company.getCompanyname());
        List<User> user_list = userMapper.selectList(userQueryWrapper);
        for(User user : user_list)
        {
            User new_user = new User(user.getUserid() , user.getUsername() , user.getNickname() , user.getPassword() , user.getPhoto() , user.getTelephone() , user.getApartmentname() , user.getGender() , user.getStatus() , user.getEmail() , user.getRole() , companyname , user.getPost() , user.getNote() , user.getCreatetime());
            userMapper.updateById(new_user);
        }


        map.put("error_message" , "success");
        return map;
    }

    @Override
    public Map<String, String> modifyAdmin(Integer companyId, String adminname) {
        Map<String, String> map = new HashMap<>();
        if (adminname == null || adminname.trim().isEmpty()) {
            map.put("error_message", "管理员名字不能为空");
            return map;
        }

        Company company = companyMapper.selectById(companyId);
        if (company == null) {
            map.put("error_message", "公司不存在");
            return map;
        }

        company.setAdminname(adminname);
        companyMapper.updateById(company);

        map.put("error_message", "success");
        return map;
    }

    public static Boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[56])|(17[0-8])|(18[0-9])|(19[1589]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
