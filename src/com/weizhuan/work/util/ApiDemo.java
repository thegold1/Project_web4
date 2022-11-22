package com.weizhuan.work.util;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ApiDemo {

    private static PoolingHttpClientConnectionManager cm = null;

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
        }
        Registry socketFactoryRegistry = RegistryBuilder.create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    }

    private static CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        return httpClient;
    }

    public static String post4Form(String uriHttp, Map map) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse resHttp = null;
        try {
            httpClient = getHttpClient();
            HttpPost post = new HttpPost(uriHttp);
            //设置参数
            List list = new ArrayList();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> elem = (Map.Entry) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            entity.setContentType("application/x-www-form-urlencoded");
            post.setEntity(entity);

            resHttp = httpClient.execute(post);
            int statusHttp = resHttp.getStatusLine().getStatusCode();
            String res = EntityUtils.toString(resHttp.getEntity(), "UTF-8");
            if (HttpStatus.SC_OK == statusHttp) {
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resHttp != null) {
                try {
                    EntityUtils.consume(resHttp.getEntity());
                    resHttp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String userId = "231500";
        String secret = "cccad692837f788120b98f13b47af4d6";
        String title = "test";
//        String content = "涵盖所有中英文类别，包括哲学、经济学、管理学、法学、社会科学、教育学、文学、艺术学、历史学、理学、工学、农学、医学、政治学、军事学等。";
        String content = "要提到国内的几所顶尖大学，首当其冲的便是清华和北大。在从小的耳濡目染下，只要是被其中的任何一所大学录取，其前途都是一片光明。不过作为国内顶尖大学，进入的门槛还是非常高的，能被录取的也都不是一般的人才。一旦毕业工作根本就不用担心，他们的起步就甩普通学校一大截。\n" +
                "\n" +
                "\n" +
                "不过近年来出现了一种现象，有许多留学生不愿意回国，这些留学生们大部分都来自于清华。这种情况令广大网友不解，这些留学生心里是怎么想的？国家为了培养他们付出了多么大的心血，结果一毕业都成了国外的人才。但凡有一点感恩之心，都应该学成归来，既可以实现自己的人生价值，又能为国家建设贡献一份力。\n" +
                "\n" +
                "一、约两万名清华留学生毕业后不愿回国，是什么原因？\n" +
                "\n" +
                "\n" +
                "如果只是寥寥几人也还行，毕竟每个人都有每个人的想法，少数的人才流失是难免的事。但是现如今人数竟高达两万，这严重现象背后的原因究竟是什么？\n" +
                "\n" +
                "1.就业环境所迫，不敢回国\n" +
                "\n" +
                "之所以留学生不回国的数量如此之大，是很多留学生看到了国内如此险峻的就业形势。随着大学生群体日渐增多，加上工作岗位实在有限，好工作越来越难找。在这样就业环境的加持下，很多留学生产生了害怕，怕自己回国后没法施展自己的鸿鹄之志。与其担惊受怕，不如就继续留在国外发展。\n" +
                "\n" +
                "\n" +
                "另外在国外工作可以给自己制造一些经验光环，毕竟有了海归身份再加上国外企业就业经验。在国内就相对来说好找工作了，具备了更多的竞争优势。所以他们选择在国外多工作几年，这样即便最后回国，在找工作时也会更有选择权。\n" +
                "\n" +
                "2.国外良好的薪资待遇和社会福利\n" +
                "\n" +
                "\n" +
                "在找工作时，都会考虑到公司所提供的薪资待遇是否理想，当然薪资越高就越会得到应聘者的青睐。在国外，学生找工作的选择更多，薪资待遇也更好，往往社会的各种福利政策层出不穷。对比国内整体的收入情况，在国外工作或许是留学生赚大钱的最佳选择。如果选择回国发展的话，就会大打折扣。\n" +
                "\n" +
                "尤其是一些家庭环境不是特别好的学生，选择在国内发展，就怕其微薄的收入不仅不能补贴家用，还会额外增加父母的负担。因为国内某些一线城市消费还是很高的，就怕出现入不敷出的情况。\n" +
                "\n" +
                "\n" +
                "也有些留学生是因为国外开放、自由的生活态度，他们在一些政策的制定上没有很严格。不管是工作、还是生活、学习、人际交往，每个人都热情洋溢，可以有自己能力的发挥空间，这样的氛围会让人感到轻松。可以明确的感到自己是被重视，被尊重了。\n" +
                "\n" +
                "不过退一万步来说，不管什么原因，留学生都不应该选择在国外发展。有一句话说得特别好“少年强则国强”，如果我国的“少年”都跑出国门建设他国了，那对于我国来说不是一笔大损失。\n" +
                "\n" +
                "\n" +
                "国家的高速发展离不开优秀的人才，特别是一些留学过的高端人才，这可都是国家花费了不少精力和财力而培养出来的。如果都选择留在国外，就会造成我国人才的流失，如果最后他们选择了定居国外，那造成的影响就更加严重了。\n" +
                "\n" +
                "3.针对这种情况国家不能坐任不管，必须要采取有效措施严加看待\n" +
                "\n" +
                "其实虽然自己身在国外，但你人还是中国人，心还是中国心。这是无法改变的事实，相信大多数的留学生还是心系祖国的。只要国家提高对留学人才的待遇，回来的可能性将大大提高，这些学生也自然愿意回国。\n" +
                "\n" +
                "\n" +
                "其次，要从小加强学生们的爱国意识，特别是对于一些即将要出国深造的留学生。其实有很多老一辈开国大将，都是在国外学生归来，放弃了外国丰厚的待遇，而选择投入祖国的建设中。所以各位莘莘学子也要像这些先驱们学习，祖国才是我们的家，我们要为中华民族的伟大复兴而奋斗。\n" +
                "\n" +
                "总结：爱国情怀一定要从小抓起，让爱国在每一个学生心里根深蒂固。这样即使飞得再远，心里也有一个牵挂的地方，身上流淌的永远都是华夏民族的血液。不管身在何处，最后都要回到祖国的怀抱。";
        Map mapPara = new HashMap<>();
        mapPara.put("userId", userId);
        mapPara.put("secret", secret);
        mapPara.put("type", "1");     // 伪原创类型，0：基础伪原创，1：原创度优先，2：可读性优先
        mapPara.put("title", title);
        mapPara.put("content", content);
        mapPara.put("useWords", "1"); // 是否使用私人词库，type!=0时有效，0：不使用，1：使用

        // 提交伪原创
        String ret = post4Form("http://api.ifagou.com/api/submitRevise.html", mapPara);
        System.out.println(ret);   // 返回json格式的数据

        mapPara.clear();
        String docIdList = "1550279884795"; // 查询的docId列表，多个以";"号隔开
        mapPara.put("userId", userId);
        mapPara.put("secret", secret);
        mapPara.put("docIdList", docIdList);

        // 获取伪原创结果
         String ret2 = post4Form("https://api.ifagou.com/api/queryReviseResult.html", mapPara);
         System.out.println(ret2);      // 返回json格式的数据

        System.out.println("tomcat here");
        String ret3 = post4Form("https://api.ifagou.com/api/queryCheckResult.html", mapPara);
        System.out.println(ret3);      // 返回json格式的数据
    }
}