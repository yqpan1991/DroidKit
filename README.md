# DroidKit
android develop tools

# 通用 loading,empty,error,正常结果页面的设计实现与竞品分析

## 1. 背景

在一般我们的App开发时,不可避免的是页面要进行loading,loading结束后,会有三个状态, 空页面, error页面,显示正常的结果页.
而在我们的普通开发时,一般是在xml中,添加一个layout,然后内部,添加 loading, empty, error, 和普通的结果页面,但是像这样的模板代码写了几遍后,发现重复的layout很多,这样会导致有几个后果
1. 设计师要求有统一的整体风格,但是不同的设计师风格是不同的,有的内容是需要定制的,既风格要统一,但是可能不千篇一律,需要可定制
2. 重复的xml的引入,一旦xml在其他的模块时,导致一个页面修改时,可能影响到多个引用相同的页面的布局


有没有比较好的设计,能够支持这种方式呢? 

这个就是咱们今天讨论的问题,以下讨论的问题包含几个方便

1. 背景
2. 市面上现有的实现方案以及他们的优缺点
3. 自己设计的实现的方案以及优缺点
4. demo工程

背景已经在上面描述,咱们直接从第2点开始

## 2. 世面上现有的实现方案以及他们的优缺点

从github以及搜索出的结果,可知,目前有三种已经开源的实现方案:

1. StatusLayoutManager
2. LoadingAndRetryManager
3. LoadSir

## 2.1 比较分析内容

咱们接下来从以下几个方面来分析这几个库

### 2.1.1 基础功能

#### 2.1.1.1 完整性
1. 状态完整性,是否包含了上述的四种状态(Loading,Empty,Error,正常result页面)
2. 是否可以自定义布局,方便定义 

#### 2.1.1.2 易用性
##### 2.1.1.2.1 接入成本

1. 是否需要更改java代码
2. 是否需要更改现有的布局
3. 侵入性

##### 2.1.1.2.2 支持程度

1. 是否支持view中的更改
2. 能否有默认的处理,方便通用的接入
3. 是否能够支持自定义的view
4. 是否可以定制listener

### 2.1.2 进阶功能

#### 2.1.2.1 加载的性能

#### 2.1.2.2 稳定性
有些状态在没有使用到时,是否可以不加载

#### 2.1.2.3 扩展性

如果要新增新的状态,是否可以很方便的增加

先给出大家一个对比的结论

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181028214109389.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0lFWVVERVlJTkpJ,size_27,color_FFFFFF,t_70)

## 2.2 StatusLayoutManager的实现与分析

### 2.2.1 代码库 

代码库地址: https://github.com/chenpengfei88/StatusLayout

### 2.2.2 代码使用与分析

https://www.jianshu.com/p/9d53893b3eda

### 2.2.3 设计思路

将几个状态的view,传入到StatusLayoutmanager中,然后开始管理

### 2.2.4 总结

这种思路是最基本的思路.自定义一个管理器,然后将外部的view直接传入,然后我们直接通过管理器封装的方法,进行操作.

优缺点: 

#### 优点

1. 状态比较完整
2. 通过java代码直接包装了view的基本的管理,方便使用
3. viewStub的使用,布局性能得到了优化
4. viewStub的使用,布局性能得到了优化
5. 可以定制 这几种状态的view

#####  缺点
1. 默认的网络错误和普通错误,设置的的onClickListener,内部帮助了实现,导致了扩展性的丢失,因为重试不一定是内部的错误,可能需要提供多种错误的,可能需要多种不同的view,点击跳转到不同的位置
2. builder中做的事情过多,比如像错误的view要展示的错误的图片,这种应该是错误的view自己来实现的,职责没有很清晰的划分
3. 除了添加java代码外,还需要将自己的layout的各个view与管理器结合起来,而且只能在java代码中实现,通用型降低,如果可以在布局中,直接初始化完成这些事情,会降低使用成本


## 2.3  LoadingAndRetryManager的实现与分析

### 2.3.1 代码库 
代码库地址: https://github.com/hongyangAndroid/LoadingAndRetryManager
### 2.3.2 代码使用与分析
https://github.com/hongyangAndroid/LoadingAndRetryManager/blob/master/README.md
### 2.3.3 设计思路
1. 替换view,然后插入自己的定制布局,在自己的定制布局后面,进行view的状态管理
2. 通过设置回调,替换不同状态的view+通知状态变更

### 2.3.4 总结

#### 优点

1. 可以设置默认的view的布局
2. 插入时管理,也是比较方便

#### 缺点 
1. 插入布局,容易有错误(activity中,如果只想定制一部分的区域,错误传入activity,会导致布局错误, fragment的布局中,在不同的生命周期中,插入时,更改的view是不同的,上面的对比图已经表达了出来)




## 2.4  LoadSir的实现与分析

### 2.3.1 代码库 

代码库: https://github.com/KingJA/LoadSir

### 2.3.2 代码使用与分析

https://www.jianshu.com/p/2d3537101281

### 2.3.3 设计思路
1. 同LoadingAndRetryManager的设计,插入自己的布局,作为rootview,然后在自己的view的内容做动态的添加和删除
2. 多了几个操作,设置不同的callback,可以操作不同的view,并且callback中,可以自己进行定制

### 2.3.4 总结

####  优点

1. 可以设置默认的view的布局
2. 插入时管理,也是比较方便
3. 整体的设计思路,是比较解耦的,方便修改

#### 缺点
1. 代码量稍微大了一些, 学些成本有增加
2. 接入现有的代码,更改较大(参考上面的对比图)
3. 扩展性不是很好 eg:像现有的错误view中,除了点击retry外,可能有其他的跳转到系统设置,这样的扩展不是很方便
4. view 给到外部,view需要做的处理,也最好外部自己来处理

## 3. 自己设计的实现的方案以及优缺点

上面的设计方便,存在几个共同的问题
1. 认为不用布局文件是比较方便的事情,其实用布局文件,更加能将之前的代码,迁移到新的加载设计思路上
2. 将view的很多操作封装到内部,反而到不方便对view进行操作

为了解决这两个问题,我设计了一个新的思路

1. 采用布局的方式,替代现有的根部局
2. 布局中,可以采用默认属性的方式,接入现有的布局
3. 布局中,比较变化的结果页,可以还保留现有的方式
4. 支持在代码中,设置loading, empty, error,以及结果页


### 3.1 代码库地址

https://github.com/yqpan1991/DroidKit

### 3.2 使用方式

#### 3.2.1 默认behavior的方式

案例代码中 : https://github.com/yqpan1991/DroidKit/blob/master/testapplication/src/main/res/layout/fragment_loading_and_result_by_behavior.xml

	<?xml version="1.0" encoding="utf-8"?>
	<com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:id="@+id/larc_content"
	    app:loading_behavior="@string/edus_larcv_common_loading_behavior"//loading的结合的behavior
	    app:content_behavior="@string/edus_larcv_common_result_behavior"//内容结合的behavior
	    app:error_behavior="@string/edus_larcv_common_error_behavior"//错误页面的behavior
	    app:empty_behavior="@string/edus_larcv_common_empty_behavior"//通用的错误页面
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity">
	
	</com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer>
	

behavior的方式,可以大大的降低xml中代码的量,但是这种方式,也有不太好的地方,便是不同的页面中的content页面,是不同的,因而,适合将加载的view,空页面,还有错误的页面以这种方式书写

#### 3.2.2 内置view的方式

案例代码中: https://github.com/yqpan1991/DroidKit/blob/master/testapplication/src/main/res/layout/fragment_loading_and_result_by_view.xml


	<?xml version="1.0" encoding="utf-8"?>
	<com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:id="@+id/larc_content"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity">
	
	    <RelativeLayout
	        android:id="@id/dg_larc_loading_id"//加载view的id,view id是定死的,不能修改,内容是可以修改的
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <ProgressBar
	            android:id="@+id/pb_content"
	            android:layout_width="wrap_content"
	            android:layout_centerInParent="true"
	            android:layout_height="wrap_content" />
	
	    </RelativeLayout>
	
	    <RelativeLayout
	        android:id="@id/dg_larc_error_id" // 错误view的id,id是写死的,不能修改,但是内容可以自定义
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <TextView
	            android:id="@+id/tv_error"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_centerInParent="true"
	            android:text="@string/edus_empty_error" />
	
	        <TextView
	            android:id="@+id/tv_retry"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_below="@id/tv_error"
	            android:layout_centerHorizontal="true"
	            android:text="@string/edus_empty_error_retry" />
	
	    </RelativeLayout>
	
	    <RelativeLayout
	        android:id="@id/dg_larc_content_id"// 加载结果的id,id是写死的,不能修改,但是内容可以自定义
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <TextView
	            android:text="common result"
	            android:layout_centerInParent="true"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content" />
	
	    </RelativeLayout>
	
	    <RelativeLayout
	        android:id="@id/dg_larc_empty_id"//// 空页面的id,id是写死的,不能修改,但是内容可以自定义
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <TextView
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_centerInParent="true"
	            android:text="@string/edus_empty_default" />
	
	
	    </RelativeLayout>
	
	
	</com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer>
	

采用内嵌view的方式,xml的布局行数会比较多,但是会很快的接入现有的代码,但是对于通用的view,例如 加载页面, 错误页面,空页面而言,有点冗余,不方便维护

#### 3.2.3 behavior + 内置view的方式(推荐方式)


为了解决以上的两种问题,我们支持了第三种方式, behavior+内置view的方式(任意排斥性的组合即可,即包含了behavior的view,不能再使用 内置view)

对于不常变化的view, 以behavior的方式可以展示,对于常变化的content view,以内置view的方式

案例代码: https://github.com/yqpan1991/DroidKit/blob/master/testapplication/src/main/res/layout/fragment_loading_and_result_by_mix.xml

	<?xml version="1.0" encoding="utf-8"?>
	<com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:app="http://schemas.android.com/apk/res-auto"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:id="@+id/larc_content"
	    app:loading_behavior="@string/edus_larcv_common_loading_behavior"//不经常变化的loading view,以behavior的方式加载
	    app:error_behavior="@string/edus_larcv_common_error_behavior"//不经常变化的error view,以behavior的方式加载
	    app:empty_behavior="@string/edus_larcv_common_empty_behavior"//不经常变化的empty view,以behavior的方式加载
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity">
	
	    <RelativeLayout
	        android:id="@id/dg_larc_content_id"// 常变化的内容的view, 以 内嵌view的方式使用
	        android:layout_width="match_parent"
	        android:layout_height="match_parent">
	
	        <TextView
	            android:id="@+id/tv_text_1"
	            android:text="common result"
	            android:layout_centerInParent="true"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content" />
	
	        <TextView
	            android:layout_below="@id/tv_text_1"
	            android:text="common result 2"
	            android:layout_centerInParent="true"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content" />
	
	
	    </RelativeLayout>
	
	
	</com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer>
	
#### 3.2.4 java代码需要更改的内容

以上三种方式,在java代码中的管理都是相同的

1. findViewById() // 找到LoadingAndResultContainer
2. 对LoadingAndResultContainer的状态view,可以获取到 
	
		getLoadingView(),
		getContentView(),
		getEmptyView(),
		getErrorView()

3. 找到这些view后,可以对其进行找到子view,然后自定义设置点击事件
4. 状态变化时,调用显示不同的状态即可
	
		showLoading()
		showEmpty()
		showError()
		showCommonResult()
