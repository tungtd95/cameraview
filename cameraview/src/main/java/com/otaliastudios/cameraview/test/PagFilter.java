package com.otaliastudios.cameraview.test;

import com.otaliastudios.cameraview.filter.BaseFilter;
import kotlin.Metadata;

/* compiled from: PagFilter.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lcom/volio/vn/b1_project/ui/recorder_video/emoji_fillter/PagFilter;", "Lcom/otaliastudios/cameraview/filter/BaseFilter;", "()V", "getFragmentShader", "", "B1_emoji_video_1.0.1_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes4.dex */
public final class PagFilter extends BaseFilter {
    @Override // com.otaliastudios.cameraview.filter.Filter
    public String getFragmentShader() {
        return " precision mediump float;\n                   varying vec2 " + this.fragmentTextureCoordinateName + ";\n                   uniform sampler2D sTexture;\n                   void main() {\n                     vec4 c = texture2D(sTexture, " + this.fragmentTextureCoordinateName + ");\n                     gl_FragColor = vec4(c.r/c.a,c.g/c.a,c.b/c.a,c.a);\n                   }\n                   ";
    }
}
