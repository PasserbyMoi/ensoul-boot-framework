package club.ensoul.framework.aliyun.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import club.ensoul.framework.aliyun.sms.exception.AliyunSmsTemplateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AliyunTagResourcesTemplate {

    @Getter
    private static Client client;

    private static final String DEFAULT_RESOURCE_TYPE = "TEMPLATE";
    private static final String DEFAULT_PROD_CODE = "dysms";

    public AliyunTagResourcesTemplate(Client client) {
        AliyunTagResourcesTemplate.client = client;
    }

    /**
     * 给模板添加标签 <br/>
     *
     * @param tagMap      标签键值对。最大个数20
     * @param resourceIds 短信模板Code
     * @return {@link TagResourcesResponseBody}<p/>
     */
    public TagResourcesResponseBody addSmsTemplate(Map<String, String> tagMap, Iterable<String> resourceIds) {

        List<TagResourcesRequest.TagResourcesRequestTag> tags = tagMap.entrySet().stream().map(o -> {
            TagResourcesRequest.TagResourcesRequestTag tag = new TagResourcesRequest.TagResourcesRequestTag();
            tag.setKey(o.getKey());
            tag.setValue(o.getValue());
            return tag;
        }).collect(Collectors.toList());

        TagResourcesRequest tagResourcesRequest = new TagResourcesRequest();
        tagResourcesRequest.setTag(tags);

        tagResourcesRequest.setResourceId(toList(resourceIds));
        tagResourcesRequest.setResourceType(DEFAULT_RESOURCE_TYPE);
        tagResourcesRequest.setRegionId(client._regionId);
        tagResourcesRequest.setProdCode(DEFAULT_PROD_CODE);
        try {
            TagResourcesResponse smsTemplateResponse = client.tagResources(tagResourcesRequest);
            return smsTemplateResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

    /**
     * 删除模板标签 <br/>
     *
     * @param unTagAll    是否删除该模板下的所有标签。true：是 false：否
     * @param tagKeys     标签键。最大个数20
     * @param resourceIds 短信模板 Code
     * @return {@link UntagResourcesResponseBody}<p/>
     */
    public UntagResourcesResponseBody untagResources(boolean unTagAll, Iterable<String> tagKeys, Iterable<String> resourceIds) {
        UntagResourcesRequest resourcesRequest = new UntagResourcesRequest();
        resourcesRequest.setAll(unTagAll);
        resourcesRequest.setTagKey(toList(tagKeys));
        resourcesRequest.setResourceId(toList(resourceIds));
        resourcesRequest.setResourceType(DEFAULT_RESOURCE_TYPE);
        resourcesRequest.setRegionId(client._regionId);
        resourcesRequest.setProdCode(DEFAULT_PROD_CODE);
        try {
            UntagResourcesResponse resourcesResponse = client.untagResources(resourcesRequest);
            return resourcesResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

    /**
     * 查询模板标签 <br/>
     *
     * @param nextToken   下一次查询的Token
     * @param pageSize    每页显示条数
     * @param tagMap      标签键值对。最大个数20
     * @param resourceIds 短信模板CODE
     * @return {@link ListTagResourcesResponseBody}<p/>
     */
    public ListTagResourcesResponseBody querySmsTemplate(String nextToken, Integer pageSize, Map<String, String> tagMap, Iterable<String> resourceIds) {

        List<ListTagResourcesRequest.ListTagResourcesRequestTag> tags = tagMap.entrySet().stream().map(o -> {
            ListTagResourcesRequest.ListTagResourcesRequestTag tag = new ListTagResourcesRequest.ListTagResourcesRequestTag();
            tag.setKey(o.getKey());
            tag.setValue(o.getValue());
            return tag;
        }).collect(Collectors.toList());

        ListTagResourcesRequest tagResourcesRequest = new ListTagResourcesRequest();
        tagResourcesRequest.setTag(tags);
        tagResourcesRequest.setResourceId(toList(resourceIds));
        tagResourcesRequest.setNextToken(nextToken);
        tagResourcesRequest.setPageSize(pageSize);
        tagResourcesRequest.setResourceType(DEFAULT_RESOURCE_TYPE);
        tagResourcesRequest.setRegionId(client._regionId);
        tagResourcesRequest.setProdCode(DEFAULT_PROD_CODE);
        try {
            ListTagResourcesResponse tagResourcesResponse = client.listTagResources(tagResourcesRequest);
            return tagResourcesResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }


    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        if (iterable.iterator().hasNext()) {
            return null;
        }
        for (T t : iterable) {
            list.add(t);
        }
        return list;
    }

}
