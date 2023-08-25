# 项目简介

novel-project是基于时下**最新**Java技术栈 Spring Boot 3 + Vue3 开发的前后端分离的小说网站项目，由小说门户系统、作家后台管理系统以及平台后台管理系统(待开发)三个子系统构成。包括小说推荐、作品检索、 小说排行榜、小说阅读、小说评论、作家专区和新闻发布等功能。



![image-20230822161529344](D:\Mycode\github-project\novel-project\img\image-20230822161529344.png)

# 项目架构

### 技术选型

|     技术     |  版本   |         说明          |
| :----------: | :-----: | :-------------------: |
| Spring Boot  |  3.0.0  |     容器+MVC框架      |
|   MyBatis    |  3.5.9  |        ORM框架        |
| MyBatis-Plus |  3.5.3  |    MyBatis增强工具    |
|     JJWT     | 0.11.5  |      JWT登录支持      |
|    MySQL     |   8.0   |      数据库支持       |
|    Redis     |   6.2   |    分布式缓存支持     |
|   Redisson   | 3.19.1  |     分布式锁实现      |
|   Caffine    |  3.1.0  |    高性能本地缓存     |
|   RabbitMQ   | 3.10.2  |      消息中间件       |
|    Lombok    | 1.18.24 |   简化对象封装工具    |
|     Gson     |  2.8.9  | Java序列化/反序列化库 |
|   Knife4j    |  4.1.0  |       接口文档        |



### 功能概要

- 前台门户系统
  - **首页：** 轮播图、本周推荐、热门推荐、精品推荐、点击榜单、新书榜单、更新榜单、最新新闻、友情链接、反馈留言
  - **新闻模块：** 新闻分类、新闻列表、新闻阅读
  - **小说检索：** 根据书名、作者名等关键词和作品频道、分类、是否完结、字数、更新方式等筛选条件检索小说
  - **小说详情页：** 小说信息展示、作家信息展示、最新章节概要、最新评论、评论发表、加入书架、同类推荐
  - **小说评论页：** 小说评论区，评论展示、发表评论
  - **小说目录页：** 小说目录展示
  - **小说内容页：** 小说章节订阅、小说内容阅读、小说段落评论
  - **排行榜：** 点击榜、更新榜、新书榜、评论榜
- 作家后台管理系统
  - **作家注册**：作家信息提交
  - **小说管理：** 小说发布、章节管理、作品信息



### 包结构

``` 
 +- com
     +- feiyu   
        +- novel
            +- NovelApplication.java -- 项目启动类
            |
            +- core -- 项目核心模块，包括各种工具、配置和常量等
            |   +- common -- 业务无关的通用模块
            |   |   +- exception -- 通用异常处理
            |   |   +- constant -- 通用常量   
            |   |   +- req -- 通用请求数据格式封装，例如分页请求数据  
            |   |   +- resp -- 接口响应工具及响应数据格式封装 
            |   |   +- util -- 通用工具   
            |   | 
            |   +- auth -- 用户认证授权相关
            |   +- config -- 业务相关配置
            |   +- constant -- 业务相关常量         
            |   +- filter -- 过滤器 
            |   +- interceptor -- 拦截器
            |   +- json -- JSON 相关的包，包括序列化器和反序列化器 
            |   +- util -- 业务相关工具 
            |   +- wrapper -- 装饰器
            |   +-annotation 自定义注解
            |   +-aspect AOP切面类
            |   +-listener 消息队列监听器
            |
            +- dto -- 数据传输对象，包括对各种 Http 请求和响应数据的封装
            |   +- req -- Http 请求数据封装
            |   +- resp -- Http 响应数据封装
            |
            +- dao -- 数据访问层，与底层 MySQL 进行数据交互
            +- manager -- 通用业务处理层，对 Service 层通用能力的下沉以及对多个 DAO 的组合复用 
            +- service -- 相对具体的业务逻辑服务层  
            +- controller -- 主要是处理各种 Http 请求，各类基本参数校验，或者不复用的业务简单处理，返回 JSON 数据等
            |   +- front -- 小说门户相关接口
            |   +- author -- 作家管理后台相关接口  
            |   +- admin   平台管理后台相关接口
```



### 数据库设计

#### 首页模块



#### 新闻模块



#### 小说模块





#### 用户模块



#### 作家模块





# 项目核心

## 1.多系统环境下的统一认证授权

#### 基于JWT实现基本的登录注册

JWT是一个比较标准的认证解决方案，这个技术在Java圈里应该用的是非常普遍的。JWT签名是一种MAC（[Message Authentication Code](https://en.wikipedia.org/wiki/Message_authentication_code)）的方法。JWT的签名流程一般是下面这个样子：

1. 用户使用用户名和口令到认证服务器上请求认证。
2. 认证服务器验证用户名和口令后，以服务器端生成JWT Token，这个token的生成过程如下：
   - 认证服务器会生成一个 Secret Key（密钥）
   - 对JWT Header和 JWT Payload分别求Base64。在Payload可能包括了用户的抽象ID和过期时间。
   - 用密钥对JWT签名 `HMAC-SHA256(SecertKey, Base64UrlEncode(JWT-Header)+'.'+Base64UrlEncode(JWT-Payload));`
3. 然后把 `base64(header).base64(payload).signature` 作为 JWT token返回客户端。
4. 客户端使用JWT Token向应用服务器发送相关的请求。这个JWT Token就像一个临时用户权证一样。

当应用服务器收到请求后：

1. 应用服务会检查 JWT Token，确认签名是正确的。
2. 然而，因为只有认证服务器有这个用户的Secret Key（密钥），所以，应用服务器得把JWT Token传给认证服务器。
3. 认证服务器通过JWT Payload 解出用户的抽象ID，然后通过抽象ID查到登录时生成的Secret Key，然后再来检查一下签名。
4. 认证服务器检查通过后，应用服务就可以认为这是合法请求了。

上面的这个过程，是在认证服务器上为用户动态生成 Secret Key的，应用服务在验签的时候，需要到认证服务器上去签，这个过程增加了一些网络调用。本项目中对这个密钥是写死在后端配置文件里的。

而由于**服务端只生成和验证 JWT，客户端保存 JWT，所以 JWT 是无状态的**，因此特别适用于分布式站点的单点登录（SSO）场景，已经成为了目前分布式服务权限控制解决方案的事实标准。本novel 项目是一个多系统并且后期会拓展为微服务架构的项目，所以使用 JWT 来实现登录认证。

**注册接口实现**：

前端请求DTO类设计如下：

```Java
@Data
public class UserRegisterReqDto {

    @Schema(description = "手机号", required = true)
    @NotBlank(message="手机号不能为空！")
    @Pattern(regexp="^1[3|4|5|6|7|8|9][0-9]{9}$",message="手机号格式不正确！")
    private String username;

    @Schema(description = "密码", required = true)
    @NotBlank(message="密码不能为空！")
    private String password;

    @Schema(description = "验证码", required = true)
    @NotBlank(message="验证码不能为空！")
    @Pattern(regexp="^\\d{4}$",message="验证码格式不正确！")
    private String velCode;

    /**
     * 请求会话标识，用来标识图形验证码属于哪个会话
     * */
    @Schema(description = "sessionId", required = true)
    @NotBlank
    @Length(min = 32,max = 32)
    private String sessionId;

}
```

注册的核心逻辑：

```Java
public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
        // 校验图形验证码是否正确
        if (!verifyCodeManager.imgVerifyCodeOk(dto.getSessionId(), dto.getVelCode())) {
            // 图形验证码校验失败
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        // 校验手机号是否已注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            // 手机号已注册
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        // 注册成功，保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfo.setUsername(dto.getUsername());
        userInfo.setNickName(dto.getUsername());
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);

        // 删除验证码
        verifyCodeManager.removeImgVerifyCode(dto.getSessionId());

        // 生成JWT 并返回
        return RestResp.ok(
                UserRegisterRespDto.builder()
                        .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                        .uid(userInfo.getId())
                        .build()
        );

    }
```

**图片验证码获取接口：**

我们需要一个图形验证码来防止用户利用机器人自动注册。该图形验证码由服务端生成，并保存在Redis中，当用户申请注册时必须带上验证码，由服务端来校验验证码的有效性，只有验证码匹配才能允许用户注册。关键点如下：

- 在保存验证码的时候需要一个全局唯一的 sessionId 字符串用于标识该验证码属于哪个浏览器会话，
- 项目中这个sessionId 由MyBatis-Plus框架中的IdWorker工具类生成，它底层使用了雪花算法来生成分布式ID，
- 该 sessionId 会在验证码返回给前端的时候一并返回，在用户提交注册的时候，这个 sessionId 也会和验证码一起提交用于校验。

```Java
@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 生成图形验证码，并放入 Redis 中
     */
    public String genImgVerifyCode(String sessionId) throws IOException {
        String verifyCode = ImgVerifyCodeUtils.getRandomVerifyCode(4);
        String img = ImgVerifyCodeUtils.genVerifyCodeImg(verifyCode);
        stringRedisTemplate.opsForValue().set(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId
                , verifyCode, Duration.ofMinutes(5));
        return img;
    }

    /**
     * 校验图形验证码
     */
    public boolean imgVerifyCodeOk(String sessionId, String verifyCode) {
        return Objects.equals(
                stringRedisTemplate.opsForValue().get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId)
                , verifyCode);
    }

    /**
     * 从 Redis 中删除验证码
     */
    public void removeImgVerifyCode(String sessionId) {
        stringRedisTemplate.delete(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + sessionId);
    }
}
```

注：我们在保存验证码的时候需要一个全局唯一的 sessionId 字符串用于标识该验证码属于哪个浏览器会话，该 sessionId 会在验证码返回给前端的时候一并返回，在用户提交注册的时候，该 sessionId 会和验证码一起提交用于校验。生成的验证码是保存在Redis中的，注册成功后会删除。

用户登录是一个特殊接口，不是单独请求一个资源，而是对于用户信息验证，创建一个 JWT，而且登录提交的数据中还包含敏感数据（密码）。URL 带的参数必须无敏感信息或符合安全要求，所以我们需要定义 POST 类型的接口来处理登录请求。具体实现略。

#### 多系统环境下的重构

​		本小说项目有平台后台、前台门户、作家后台等多个系统，若把所有系统的认证授权逻辑都在一个方法中，将有太多的if-else逻辑，代码臃肿难以维护。每当某一系统授权逻辑发生变化或者新增加了一个子系统，都需要修改此处的代码。而此时就想到策略模式，它的核心在于封装变化，在我们系统中就是定义多个不同系统的认证授权策略（算法族），分别封装成独立的类。拦截器（客户）在运行时根据具体的请求 URI 来动态调用相应系统的认证授权算法。当某一系统的认证授权逻辑发生变化或增加新的子系统时，我们只需要修改或增加相应的策略类，而不会影响到其它的策略类（子系统）和客户（拦截器）。

​		首先定义接口类：包括一个统一的单点登录认证逻辑和一个独立的认证方法（子系统重写）。

```java
/**
 * 策略模式实现用户认证授权
 */
public interface AuthStrategy {

    /**
     * 独立的用户认证逻辑
     * @param token
     * @param requestUri
     * @throws BusinessException
     */
    void auth(String token,String requestUri)throws BusinessException;

    /**
     * 默认的通用单点登录认证授权（适用于前台门户、作家系统等多个系统）
     * @param jwtUtils
     * @param token
     * @param userInfoCacheManager
     * @return
     */
    default Long authSSO(JwtUtils jwtUtils,String token,UserInfoCacheManager userInfoCacheManager){
        //token为空
        if(!StringUtils.hasText(token)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        //token存在，则解析出用户ID
        Long userId = jwtUtils.parseToken(token, SystemConfigConsts.NOVEL_FRONT_KEY);
        if(Objects.isNull(userId)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        UserInfoDto user = userInfoCacheManager.getUser(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        //用户存在，则设置userid到前线程
        UserHolder.setUserId(userId);
        //返回userId
        return userId;
    }
}
```

然后接着定义前台门户、作家后台、平台管理等的具体策略类，上述实现接口，重写auth方法。代码略

最后在拦截器中根据请求 URI 动态调用相应策略：

```java
/**
 * 认证授权 拦截器
 * 为了注入其它的 Spring beans，需要通过 @Component 注解将该拦截器注册到 Spring 上下文
 *
 * @author gsj
 * @date 2023/5/18
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final Map<String, AuthStrategy> authStrategy;

    private final ObjectMapper objectMapper;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取登录 JWT
        String token = request.getHeader(SystemConfigConsts.HTTP_AUTH_HEADER_NAME);

        // 获取请求的 URI
        String requestUri = request.getRequestURI();

        // 根据请求的 URI 得到认证策略
        String subUri = requestUri.substring(ApiRouterConsts.API_URL_PREFIX.length() + 1);
        String systemName = subUri.substring(0,subUri.indexOf("/"));
        String authStrategyName = String.format("%sAuthStrategy",systemName);

        // 开始认证
        try {
            authStrategy.get(authStrategyName).auth(token,requestUri);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }catch (BusinessException exception){
            // 认证失败
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(RestResp.fail(exception.getErrorCodeEnum())));
            return false;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 清理当前线程保存的用户数据
        UserHolder.clear();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
```





## 2.小说副本数据的缓存

本项目虽然是单体架构，但是有很多数据经常被访问，如首页的小说推荐、排行榜等等，考虑到以下原因，我不会单独只使用Redis来做缓存：

1. Redis如果挂了或者使用老版本的Redis,其会进行全量同步，此时Redis是不可用的，这个时候我们只能访问数据库，很容易造成雪崩。
2. 访问Redis会有一定的网络I/O以及序列化反序列化，虽然性能很高但是其终究没有本地方法快，可以将最热的数据存放在本地，以便进一步加快访问速度。这个思路并不是我们做互联网架构独有的，在计算机系统中使用L1,L2,L3多级缓存，用来减少对内存的直接访问，从而加快访问速度。

#### 本地缓存的选型

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/8/16/165406d9fff73da6~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.png)

Caffeine是当前最优秀的内存缓存框架，不论读还是写的效率都远高于其他缓存，而且在Spring5开始的默认缓存实现就将Caffeine代替原来的Google Guava。我在项目中使用Caffeine来缓存诸如首页小说推荐、小说新书排行榜、小说信息、作家基本信息等热点数据，具体实现方式是基于Spring Cache注解。

首先引入Caffeine和Spring Cache依赖，

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

然后自定义本地缓存管理器：

```java
/**
    * Caffeine 缓存管理器
    */
@Bean
public CacheManager caffeineCacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();

    List<CaffeineCache> caches = new ArrayList<>(CacheConsts.CacheEnum.values().length);
    for (CacheConsts.CacheEnum c : CacheConsts.CacheEnum.values()) {
        if (c.isLocal()) {
            Caffeine<Object, Object> caffeine = Caffeine.newBuilder().recordStats().maximumSize(c.getMaxSize());
            if (c.getTtl() > 0) {
                caffeine.expireAfterWrite(Duration.ofSeconds(c.getTtl()));
            }
            caches.add(new CaffeineCache(c.getName(), caffeine.build()));
        }
    }

    cacheManager.setCaches(caches);
    return cacheManager;
}
```

注：在循环遍历枚举值时，根据每个枚举值的配置创建相应的 `CaffeineCache` 对象，并将其添加到缓存管理器中。这样，我们可以根据需要对各个缓存进行配置（如缓存最大容量、过期时间等），并使用相应的缓存名称进行访问。

接着使用 @EnableCaching 注解开启缓存后，就可以根据不同业务服务添加缓存功能，例如对于小说信息的查询，可设置成如下：

```Java
/**
     * 从缓存中查询小说信息（先判断缓存中是否已存在，存在则直接从缓存中取，否则执行方法体中的逻辑）
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long id) {
        return cachePutBookInfo(id);
    }

    /**
     * 缓存小说信息（不管缓存中是否存在都执行方法体中的逻辑，然后缓存起来）
     */
    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto cachePutBookInfo(Long id) {
        // 查询基础信息
        BookInfo bookInfo = bookInfoMapper.selectById(id);
        // 查询首章ID（取升序结果的第一个）
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, id)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter firstBookChapter = bookChapterMapper.selectOne(queryWrapper);
        // 组装响应对象
        return BookInfoRespDto.builder()
                .id(bookInfo.getId())
                .bookName(bookInfo.getBookName())
                .bookDesc(bookInfo.getBookDesc())
                .bookStatus(bookInfo.getBookStatus())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .categoryId(bookInfo.getCategoryId())
                .categoryName(bookInfo.getCategoryName())
                .commentCount(bookInfo.getCommentCount())
                .firstChapterId(firstBookChapter.getId())
                .lastChapterId(bookInfo.getLastChapterId())
                .picUrl(bookInfo.getPicUrl())
                .visitCount(bookInfo.getVisitCount())
                .wordCount(bookInfo.getWordCount())
                .build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfoCache(Long ignoredId) {
        // 调用此方法自动清除小说信息的缓存
    }
```

#### 分布式缓存

本地缓存虽然有着访问速度快的优点，但无法进行大数据的存储。并且当我们集群部署多个服务节点，或者后期随着业务发展进行服务拆分后，没法共享缓存和保证缓存数据的一致性。 本地缓存的数据还会随应用程序的重启而丢失，这样对于需要持久化的数据满足不了需求，还会导致重启后数据库瞬时压力过大。

所以本地缓存一般适合于缓存只读数据，如统计类数据，或者每个部署节点独立的数据。其它情况就需要用到分布式缓存了。

上述使用本地缓存的大多针对于当前访问热度最高并且更新不频繁的数据，而对于一些经常更新的数据，比如小说点击排行榜，可以用Redis缓存，并设置较短的过期时间。代码略。

#### 缓存的异步刷新

为了实现在不修改调用方（所有小说信息发生变更的地方。例如，作家更新小说信息、作家发布新的章节或平台下架违规小说等场景）代码，不影响原先功能（其它副本数据的刷新）的同时，又能及时刷新 MongoDB 中的副本数据，实现模块间的解耦，我们使用消息中间件RabbitMQ来解决上述问题。

首先定义相关配置类如下：

```Java
/**
 * 配置小说相关缓存更新的交换机、队列以及绑定关系
 * 发布/订阅模型
 * 交换机类型：广播(FanoutExchange)
 */
@Configuration
public class AmqpConfig {

    @Bean
    public FanoutExchange bookCacheUpdateExchange(){
        return new FanoutExchange("novel.fanout");
    }

    @Bean
    public Queue bookCacheUpdateQueue(){
        return new Queue("bookCache.queue");
    }

    @Bean
    public Binding bindingCacheUptQueue(Queue bookCacheUpdateQueue,FanoutExchange bookCacheUpdateExchange){
        return BindingBuilder.bind(bookCacheUpdateQueue).to(bookCacheUpdateExchange);
    }
}
```

配置消息发送类：

```Java
@Component
@RequiredArgsConstructor
public class AmqpMsgManager {

    private final AmqpTemplate amqpTemplate;

    /**
     * 发送小说信息改变消息
     */
    public void sendBookChangeMsg(Long bookId) {
        sendAmqpMessage(amqpTemplate,"novel.fanout",null,bookId);
    }

    private void sendAmqpMessage(AmqpTemplate amqpTemplate, String exchange, String routingKey, Object message) {
        // 如果在事务中则在事务执行完成后再发送，否则可以直接发送
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    amqpTemplate.convertAndSend(exchange, routingKey, message);
                }
            });
            return;
        }
        amqpTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("小说章节更新了！");
    }

}
```

配置队列监听器

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitQueueListener {

    private final BookInfoCacheManager bookInfoCacheManager;

    private final BookInfoMapper bookInfoMapper;

    @RabbitListener(queues = "bookCache.queue")
    public void listenFanoutQueue1(Long msgBookId) {

        //更新对应id的小说信息缓存
        bookInfoCacheManager.cachePutBookInfo(msgBookId);

        log.info("监听器接收消息后更新缓存：\n" + bookInfoMapper.selectById(msgBookId).toString() + "");

    }
}
```





## 3.分布式锁防止重复发表评论

在小说网站中，对一部作品的评论，我们这里限制单用户只能评论一次，为此需要保证接口的幂等性，防止用户短时间内多次重复发表，下面基于Redisson+AOP+自定义注解实现分布式锁，对用户评论接口进行限制。

引入依赖如下：

```xml
<!-- Redisson 相关 -->
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson-spring-boot-starter</artifactId>
            <version>${redisson.version}</version>
        </dependency>

        <!-- Aop 相关 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
```

1.先定义了以下两个注解类：

```java
@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Key {

    String expr() default "";

}

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Lock {

    String prefix();

    boolean isWait() default false;

    long waitTime() default 3L;

    ErrorCodeEnum failCode() default ErrorCodeEnum.OK;
}
```

@Key 注解定义了一个属性expr()，用于指定锁的key值表达式，即在分布式系统中唯一标识某个资源的字符串。可以用在方法、字段和参数上。

@Lock 注解定义了以下几个属性：

- prefix：指定锁的前缀，用于区分不同的锁。
- isWait：是否等待获取锁。默认为false，如果为true，则会在锁被其他线程占用时等待一定时间再尝试获取锁。
- waitTime：等待获取锁的时间，单位为秒，默认为3秒。
- failCode：获取锁失败后返回的错误码，默认为OK。

通过在需要加锁的方法上添加@Lock注解，并指定prefix值和isWait等属性，可以实现分布式锁的获取和释放。同时，使用@Key注解指定锁的key值表达式，可以确保不同的锁之间不会互相影响。

2.定义切面类

```java
@Aspect // 声明为切面类
@Component // 声明为Spring组件，可以被自动扫描到
@RequiredArgsConstructor // 会生成一个带有final字段的构造函数
public class LockAspect {
  
    private final RedissonClient redissonClient; // 使用Redisson实现分布式锁
  
    private static final String KEY_PREFIX = "Lock"; // 锁的默认前缀
  
    private static final String KEY_SEPARATOR = "::"; // 锁key值表达式中的分隔符

    // 定义Around类型的切点，匹配所有使用@Lock注解的方法
    @Around(value = "@annotation(com.feiyu.novel.core.annotation.Lock)")
    @SneakyThrows // Lombok注解，自动处理Checked Exception
    public Object doAround(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Lock lock = targetMethod.getAnnotation(Lock.class); // 获取@Lock注解
        String lockKey = KEY_PREFIX + buildLockKey(lock.prefix(), targetMethod,
            joinPoint.getArgs()); // 构建锁的key值
        RLock rLock = redissonClient.getLock(lockKey); // 获取可重入锁对象
        if (lock.isWait() ? rLock.tryLock(lock.waitTime(), TimeUnit.SECONDS) : rLock.tryLock()) { // 尝试获取锁
            try {
                return joinPoint.proceed(); // 执行目标方法
            } finally {
                rLock.unlock(); // 释放锁
            }
        }
        throw new BusinessException(lock.failCode()); // 获取锁失败，抛出业务异常
    }

    // 根据@Key注解构建锁的key值表达式
    private String buildLockKey(String prefix, Method method, Object[] args) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.hasText(prefix)) { // 如果有指定前缀，则加入到key值表达式中
            builder.append(KEY_SEPARATOR).append(prefix);
        }
        Parameter[] parameters = method.getParameters(); // 获取方法的参数列表
        for (int i = 0; i < parameters.length; i++) {
            builder.append(KEY_SEPARATOR);
            if (parameters[i].isAnnotationPresent(Key.class)) { // 如果该参数上有@Key注解，则将其加入到key值表达式中
                Key key = parameters[i].getAnnotation(Key.class); // 获取@Key注解
                builder.append(parseKeyExpr(key.expr(), args[i])); // 解析key值表达式中的占位符，并替换为实际参数值
            }
        }
        return builder.toString(); // 返回构建的锁的key值表达式
    }

    // 解析key值表达式中的占位符，并替换为实际参数值
    private String parseKeyExpr(String expr, Object arg) {
        if (!StringUtils.hasText(expr)) { // 如果key值表达式为空，则直接返回参数值的字符串表示
            return arg.toString();
        }
        ExpressionParser parser = new SpelExpressionParser(); // 创建Spring EL表达式解析器
        Expression expression = parser.parseExpression(expr, new TemplateParserContext()); // 将key值表达式解析为Expression对象
        return expression.getValue(arg, String.class); // 将实际参数值和Expression对象传入，得到最终的key值
    }

}
```

在切面类中，首先声明了需要使用的RedissonClient对象，并定义了默认的锁前缀和key值表达式中的分隔符。

然后，通过定义Around类型（环绕通知）的切点，匹配所有使用@Lock注解的方法，并在doAround方法中实现了获取分布式锁的逻辑。在这个方法中，我们首先根据@Lock注解中指定的前缀、目标方法和方法参数，构建出一个唯一的键名，然后通过Redisson获取可重入锁对象，再尝试获取锁。如果获取成功，则执行目标方法，获取失败，则等待一定时间再次尝试。



## 4.接口防刷限流

novel 作为一个互联网系统，经常会遇到非法爬虫（例如，盗版小说网站）来爬取我们系统的小说数据，这种爬虫行为有时会高达每秒几百甚至上千次访问。防刷的目的是为了限制这些爬虫请求我们接口的频率，如果我们不做接口防刷限制的话，我们系统很容易就会被爬虫干倒。

限流的目的是在流量高峰期间，根据我们系统的承受能力，限制同时请求的数量，保证多余的请求会阻塞一段时间再处理，不简单粗暴的直接返回错误信息让客户端重试，同时又能起到流量削峰的作用。

常见的限流方案有计数器、漏桶算法、令牌桶算法等，这些适用于单机限流的场景，而本项目中用到的是基于Redis+Lua实现的分布式限流方案。而`Lua`脚本和 `MySQL`数据库的存储过程比较相似，它们执行一组命令，所有命令的执行要么全部成功或者失败，因此具有天然的原子性。

相比`Redis`事务，`Lua脚本`的优点：

- 减少网络开销：使用`Lua`脚本，无需向`Redis` 发送多次请求，执行一次即可，减少网络传输
- 原子操作：`Redis` 将整个`Lua`脚本作为一个命令执行，原子，无需担心并发
- 复用：`Lua`脚本一旦执行，会永久保存 `Redis` 中,其他客户端可复用

核心限流脚本如下：

```Java
 public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }
```

这段Lua脚本的执行逻辑如下：

1. 首先，Lua脚本通过调用Redis的get命令获取存储在指定键（KEYS[1]）中的计数器值，并将其存储在变量c中。
2. 然后，脚本检查变量c的值是否存在且大于指定的限流阈值（ARGV[1]）。如果是，则表示已经超过了限流阈值，直接返回当前计数器的值。
3. 如果变量c的值不存在或者小于等于限流阈值，脚本调用Redis的incr命令对计数器进行自增操作，并将自增后的值存储回指定的键中。
4. 接着，脚本检查自增后的计数器的值是否为1。如果是1，则表示这是第一次调用，需要设置该键的过期时间（ARGV[2]）来限制访问频率。
5. 最后，脚本返回计数器的值。

总结：通过使用Lua脚本，在Redis的原子性操作下，能够实现对计数器的读取、自增、过期时间设置等操作，从而确保在指定时间内只允许一定数量的请求通过，控制系统的访问频率，达到限流的效果。



# 项目部署

本项目基于宝塔Linux面板部署在腾讯云服务器上。

在线访问地址：

http://124.222.198.190:1024

### MySQL与Redis安装

直接在宝塔面板下载即可

### 安装jdk17

```shell
cd /usr/local
wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
tar -zxvf jdk-17_linux-x64_bin.tar.gz 
```

将jdk-17改名为java

```
mv jdk-17 java
```

进入profile文件，按i进入编辑模式

```
vim /etc/profile
```

在文件最下方添加

```
export JAVA_HOME=/usr/local/java
export PATH=$PATH:$JAVA_HOME/bin;
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar;
```

重新加载环境变量

```
source /etc/profile
```

### Nginx配置

修改监听端口，与前端访问ip对应；然后配置后端服务的反向代理

```nginx
server {
    listen       80;
    server_name  api.novel.com;
    
    location / {
        proxy_set_header   Host             $host;
#       proxy_set_header   X-Real-IP        $remote_addr;
        proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
        proxy_pass   http://192.168.0.58:8888; # 不能使用 127.0.0.1，要使用宿主机 IP
    }
}
```

### 启动后端项目

如下表示后台持续运行，

```
nohup java -jar /www/wwwroot/novel-backend.jar &
```



