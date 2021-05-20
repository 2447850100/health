import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.pojo.CheckGroup;
import com.xiaohu.service.CheckGroupService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springmvc.xml"})
public class Test {
    @Reference
    private CheckGroupService checkGroupService;
    @org.junit.Test
    public void Test(){
        List<CheckGroup> all = checkGroupService.findAll();
        System.out.println(all);
    }
}
