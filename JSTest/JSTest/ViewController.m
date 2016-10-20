//
//  ViewController.m
//  JSTest
//
//  Created by apple on 16/1/27.
//  Copyright © 2016年 河南大实惠电子商务有限公司. All rights reserved.
//

#import "ViewController.h"
#import <JavaScriptCore/JavaScriptCore.h>
@protocol JSObjcDelegate <JSExport>

- (void)callCamera;
- (void)share:(NSString *)shareString;

@end
@interface ViewController ()<UIWebViewDelegate,JSObjcDelegate>
@property(nonatomic, strong)JSContext *jsContext;
@property(nonatomic, strong)UIWebView *webView;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupSubView];
    [self loadRequestData];
}

#pragma mark -- setup SubView
- (void)setupSubView {
    _webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.bounds), CGRectGetHeight(self.view.bounds))];
    _webView.delegate = self;
    [self.view addSubview:_webView];
}
#pragma mark -- request Data
- (void)loadRequestData {
    NSURL *url = [[NSBundle mainBundle] URLForResource:@"test" withExtension:@"html"];
    NSURLRequest *request = [[NSURLRequest alloc]initWithURL:url];
    [self.webView loadRequest:request];
}
#pragma mark -- UIWebViewDelegate
- (void)webViewDidFinishLoad:(UIWebView *)webView {
    self.jsContext = [webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    self.jsContext[@"Toyun"] = self;
    self.jsContext.exceptionHandler = ^(JSContext *context ,JSValue *exceptionValue) {
        context.exception = exceptionValue;
        NSLog(@"异常信息：%@",exceptionValue);
    };
}
#pragma mark -- JSObjcDelegate
- (void)callCamera {
    NSLog(@"callCamera");
    JSValue *picCallback = self.jsContext[@"picCallback"];
    [picCallback callWithArguments:@[@"photosssss"]];
}
- (void)share:(NSString *)shareString {
    NSLog(@"share:%@", shareString);
    // 分享成功回调js的方法shareCallback
    JSValue *shareCallback = self.jsContext[@"shareCallback"];
    [shareCallback callWithArguments:nil];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
