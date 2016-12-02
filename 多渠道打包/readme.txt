
app build.gradle
	buildTypes {
        release {
            // 控制日志Log 输出打印
            buildConfigField("boolean", "enableLog", "false")
            //混淆
            minifyEnabled false
            //Zipalign优化
            zipAlignEnabled true
            // 移除无用的resource文件
            shrinkResources true
            //前一部分代表系统默认的android程序的混淆文件，该文件已经包含了基本的混淆声明
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    /*多渠道打包*/
    productFlavors {
        wandoujia {}
        baidu {}
        c360 {}
        uc {}
    }
    //批量配置
    productFlavors.all { flavor ->
        flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
    }