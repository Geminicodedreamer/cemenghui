package com.kob.backend.service.impl.organization.curd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.*;
import com.kob.backend.pojo.*;
import com.kob.backend.service.organization.curd.ModifyOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ModifyOrganizationServiceImpl implements ModifyOrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> modifyorganization(Integer id,String uporganization, String organizationname, String charger, String telephone, String email, String status) {
        Map<String ,String> map =new HashMap<>();
        if (organizationname ==null){
            map.put("error_message","部门名称不能为空");
        }

        if(charger ==null){
            map.put("error_message","负责人不能为空");
            return map;
        }

        if (telephone==null){
            map.put("error_message","联系电话不能为空");
        }

        if(email ==null){
            map.put("error_message","邮箱不能为空");
        }

        if (organizationname.length() >100){
            map.put("error_message","部门长度不能大于100");
            return map;
        }

        QueryWrapper<Organization> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("organizationname",organizationname);
        QueryWrapper<Organization> queryWrapper_id=new QueryWrapper<>();
        queryWrapper_id.eq("id",id);
        Organization organization=organizationMapper.selectOne(queryWrapper_id);
        List<Organization> organizations =organizationMapper.selectList(queryWrapper);
        if(!organizations.isEmpty() && !organization.getOrganizationname().equals(organizationname)){
            map.put("error_message","部门名称已存在");
            return map;
        }

        if(uporganization.equals("测盟会"))
        {
            QueryWrapper<Company> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("companyname" , organization.getOrganizationname());
            Company company = companyMapper.selectOne(queryWrapper2);
            Company new_company = new Company(company.getId() , organizationname , company.getPassword() , company.getPhoto() , company.getOwnername() , company.getTelephone() , company.getAdminname() , company.getSymbol() , company.getNote());
            companyMapper.updateById(new_company);

            QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
            newsQueryWrapper.eq("tenant" , organization.getOrganizationname());
            List<News> news_list = newsMapper.selectList(newsQueryWrapper);
            for(News news : news_list){
                News new_news = new News(news.getNewsid() , news.getTitle(), news.getSummary() , news.getImagePath() , news.getContent() , news.getAuthor() , organizationname);
                newsMapper.updateById(new_news);
            }

            QueryWrapper<Meeting> meetingQueryWrapper = new QueryWrapper<>();
            meetingQueryWrapper.eq("companyname" , organization.getOrganizationname());
            List<Meeting> meeting_list = meetingMapper.selectList(meetingQueryWrapper);
            for(Meeting meeting : meeting_list)
            {
                Meeting new_meeting = new Meeting(meeting.getMeetingid() , meeting.getMeetingname() , meeting.getCreator() , meeting.getContent() , meeting.getStarttime() , meeting.getEndtime() , meeting.getPhoto() , organizationname);
                meetingMapper.updateById(new_meeting);
            }

            QueryWrapper<Lesson> lessonQueryWrapper = new QueryWrapper<>();
            lessonQueryWrapper.eq("companyname" , organization.getOrganizationname());
            List<Lesson> lesson_list = lessonMapper.selectList(lessonQueryWrapper);
            for(Lesson lesson : lesson_list)
            {
                Lesson new_lesson = new Lesson(lesson.getId() , lesson.getLessonname() , lesson.getLessonintro() , lesson.getLessonauthor() , lesson.getPhoto() , lesson.getVideo() , organizationname);
                lessonMapper.updateById(new_lesson);
            }

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("companyname" , organization.getOrganizationname());
            List<User> user_list = userMapper.selectList(userQueryWrapper);
            for(User user : user_list)
            {
                User new_user = new User(user.getUserid() , user.getUsername() , user.getNickname() , user.getPassword() , user.getPhoto() , user.getTelephone() , user.getApartmentname() , user.getGender() , user.getStatus() , user.getEmail() , user.getRole() , organizationname , user.getPost() , user.getNote() , user.getCreatetime());
                userMapper.updateById(new_user);
            }

        }

        Organization new_organization =new Organization(id,uporganization,organizationname,charger,telephone,email,status,null);

        organizationMapper.updateById(new_organization);

        map.put("error_message","success");
        return map;
    }
}
