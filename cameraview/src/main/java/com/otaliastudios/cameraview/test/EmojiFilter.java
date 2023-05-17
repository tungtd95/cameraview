package com.otaliastudios.cameraview.test;

import android.content.Context;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.otaliastudios.cameraview.filter.BaseFilter;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.NoFilter;
import com.otaliastudios.cameraview.size.Size;
import com.otaliastudios.cameraview.video.encoding.TextureMediaEncoder;
import com.otaliastudios.opengl.core.Egloo;
import com.otaliastudios.opengl.program.GlProgram;
import com.otaliastudios.opengl.program.GlTextureProgram;
import com.otaliastudios.opengl.texture.GlFramebuffer;
import com.otaliastudios.opengl.texture.GlTexture;

import java.net.URI;
import java.util.List;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

import org.libpag.PAGFile;
import org.libpag.PAGImage;
import org.libpag.PAGPlayer;
import org.libpag.PAGSurface;

/* compiled from: EmojiFilter.kt */
public final class EmojiFilter implements Filter {
    private final String TAG;
    private Callback callback;
    private final Context context;
    private State defaultInputState;
    private State defaultState;
    private final Handler handle;
    private boolean isDrawPag;
    private long lastTime;
    private List<State> listState;
    private PAGFile pagFile;
    private PAGFile pagFile2;
    private final PAGPlayer pagPlayer;
    private State pagState;
    private PAGSurface pagSurface;
    private float progress;
    private State state1;
    private State state2;
    private State state3;
    private State state4;
    private Status status;
    private float timeSpace;

    /* compiled from: EmojiFilter.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, d2 = {"Lcom/volio/vn/b1_project/ui/recorder_video/emoji_fillter/EmojiFilter$Callback;", "", "onEnd", "", "onStart", "B1_emoji_video_1.0.1_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes4.dex */
    public interface Callback {

        /* compiled from: EmojiFilter.kt */
        @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
        /* loaded from: classes4.dex */
        public static final class DefaultImpls {
            public static void onEnd(Callback callback) {
            }

            public static void onStart(Callback callback) {
            }
        }

        void onEnd();

        void onStart();
    }

    /* compiled from: EmojiFilter.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lcom/volio/vn/b1_project/ui/recorder_video/emoji_fillter/EmojiFilter$Status;", "", "(Ljava/lang/String;I)V", "PREPARE", "START", "RECODING", "END", "B1_emoji_video_1.0.1_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes4.dex */
    public enum Status {
        PREPARE,
        START,
        RECODING,
        END
    }

    public static /* synthetic */ void $r8$lambda$1KIM4pF6CgprFNSvSU1BUhNcgQQ(EmojiFilter emojiFilter) {
        m561start$lambda3(emojiFilter);
    }

    public static /* synthetic */ void $r8$lambda$e5y7GarrupkbrUoPYi6RtF0QIlg(EmojiFilter emojiFilter) {
        m562updateProgressValue$lambda2(emojiFilter);
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public String getFragmentShader() {
        return GlTextureProgram.SIMPLE_FRAGMENT_SHADER;
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public String getVertexShader() {
        return GlTextureProgram.SIMPLE_VERTEX_SHADER;
    }

    /* compiled from: EmojiFilter.kt */
    @Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0014\n\u0002\b\b\b\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010'\u001a\u00020(J\u0006\u0010)\u001a\u00020(J\u0006\u0010*\u001a\u00020\u0005J\u0016\u0010+\u001a\u00020(2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/J\u0006\u00100\u001a\u00020(J\u0006\u00101\u001a\u00020(J\u0006\u00102\u001a\u00020(J\u0006\u00103\u001a\u00020(J\u0016\u0010%\u001a\u00020(2\u0006\u00104\u001a\u00020\u001c2\u0006\u00105\u001a\u00020\u001cJ\u0006\u00106\u001a\u00020(R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\"X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&¨\u00067"}, d2 = {"Lcom/volio/vn/b1_project/ui/recorder_video/emoji_fillter/EmojiFilter$State;", "", TextureMediaEncoder.FILTER_EVENT, "Lcom/otaliastudios/cameraview/filter/BaseFilter;", "isSampler2D", "", "(Lcom/otaliastudios/cameraview/filter/BaseFilter;Z)V", "getFilter", "()Lcom/otaliastudios/cameraview/filter/BaseFilter;", "setFilter", "(Lcom/otaliastudios/cameraview/filter/BaseFilter;)V", "isChangeSize", "()Z", "setChangeSize", "(Z)V", "outputFramebuffer", "Lcom/otaliastudios/opengl/texture/GlFramebuffer;", "getOutputFramebuffer", "()Lcom/otaliastudios/opengl/texture/GlFramebuffer;", "setOutputFramebuffer", "(Lcom/otaliastudios/opengl/texture/GlFramebuffer;)V", "outputTexture", "Lcom/otaliastudios/opengl/texture/GlTexture;", "getOutputTexture", "()Lcom/otaliastudios/opengl/texture/GlTexture;", "setOutputTexture", "(Lcom/otaliastudios/opengl/texture/GlTexture;)V", "program", "", "getProgram", "()I", "setProgram", "(I)V", "size", "Lcom/otaliastudios/cameraview/size/Size;", "getSize", "()Lcom/otaliastudios/cameraview/size/Size;", "setSize", "(Lcom/otaliastudios/cameraview/size/Size;)V", "bindBuffer", "", "bindTexture", "createFrameBuffer", "draw", "timestampUs", "", "transformMatrix", "", "onCreate", "release", "releaseBufferTexture", "setCallback", "width", "height", "useProgram", "B1_emoji_video_1.0.1_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes4.dex */
    public static final class State {
        private BaseFilter filter;
        private boolean isChangeSize;
        private final boolean isSampler2D;
        private GlFramebuffer outputFramebuffer;
        private GlTexture outputTexture;
        private int program;
        private Size size;

        public State() {
            this(null, false, 3, null);
        }

        public final void setCallback() {
        }

        public State(BaseFilter filter, boolean z) {
            Intrinsics.checkNotNullParameter(filter, "filter");
            this.filter = filter;
            this.isSampler2D = z;
            this.size = new Size(1, 1);
            this.program = -1;
        }

        public /* synthetic */ State(NoFilter noFilter, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? new NoFilter() : noFilter, (i & 2) != 0 ? true : z);
        }

        public final BaseFilter getFilter() {
            return this.filter;
        }

        public final void setFilter(BaseFilter baseFilter) {
            Intrinsics.checkNotNullParameter(baseFilter, "<set-?>");
            this.filter = baseFilter;
        }

        public final boolean isChangeSize() {
            return this.isChangeSize;
        }

        public final void setChangeSize(boolean z) {
            this.isChangeSize = z;
        }

        public final Size getSize() {
            return this.size;
        }

        public final void setSize(Size size) {
            Intrinsics.checkNotNullParameter(size, "<set-?>");
            this.size = size;
        }

        public final int getProgram() {
            return this.program;
        }

        public final void setProgram(int i) {
            this.program = i;
        }

        public final GlFramebuffer getOutputFramebuffer() {
            return this.outputFramebuffer;
        }

        public final void setOutputFramebuffer(GlFramebuffer glFramebuffer) {
            this.outputFramebuffer = glFramebuffer;
        }

        public final GlTexture getOutputTexture() {
            return this.outputTexture;
        }

        public final void setOutputTexture(GlTexture glTexture) {
            this.outputTexture = glTexture;
        }

        public final void setSize(int i, int i2) {
            if (i == this.size.getWidth() && i2 == this.size.getHeight()) {
                return;
            }
            this.isChangeSize = true;
            this.size = new Size(i, i2);
        }

        public final void useProgram() {
            GLES20.glUseProgram(this.program);
        }

        public final void bindBuffer() {
            GlFramebuffer glFramebuffer = this.outputFramebuffer;
            if (glFramebuffer != null) {
                glFramebuffer.bind();
            }
        }

        public final void draw(long j, float[] transformMatrix) {
            Intrinsics.checkNotNullParameter(transformMatrix, "transformMatrix");
            this.filter.draw(j, transformMatrix);
        }

        public final void bindTexture() {
            GlTexture glTexture = this.outputTexture;
            if (glTexture != null) {
                glTexture.bind();
            }
        }

        public final void onCreate() {
            int create;
            if (this.isSampler2D) {
                GlProgram.Companion companion = GlProgram.Companion;
                String vertexShader = this.filter.getVertexShader();
                Intrinsics.checkNotNullExpressionValue(vertexShader, "filter.vertexShader");
                String fragmentShader = this.filter.getFragmentShader();
                Intrinsics.checkNotNullExpressionValue(fragmentShader, "filter.fragmentShader");
                create = companion.create(vertexShader, fragmentShader.replace("samplerExternalOES ", "sampler2D "));
            } else {
                GlProgram.Companion companion2 = GlProgram.Companion;
                String vertexShader2 = this.filter.getVertexShader();
                Intrinsics.checkNotNullExpressionValue(vertexShader2, "filter.vertexShader");
                String fragmentShader2 = this.filter.getFragmentShader();
                Intrinsics.checkNotNullExpressionValue(fragmentShader2, "filter.fragmentShader");
                create = companion2.create(vertexShader2, fragmentShader2);
            }
            this.program = create;
            this.filter.onCreate(create);
        }

        public final void releaseBufferTexture() {
            GlFramebuffer glFramebuffer = this.outputFramebuffer;
            if (glFramebuffer != null) {
                glFramebuffer.release();
            }
            GlTexture glTexture = this.outputTexture;
            if (glTexture != null) {
                glTexture.release();
            }
        }

        public final void release() {
            releaseBufferTexture();
            this.size = new Size(0, 0);
        }

        public final boolean createFrameBuffer() {
            if (this.isChangeSize) {
                this.isChangeSize = false;
                releaseBufferTexture();
                this.outputTexture = new GlTexture(GLES20.GL_TEXTURE0,
                        GLES20.GL_TEXTURE_2D, this.size.getWidth(), this.size.getHeight());
                GlFramebuffer glFramebuffer = new GlFramebuffer();
                this.outputFramebuffer = glFramebuffer;
                GlTexture glTexture = this.outputTexture;
                Intrinsics.checkNotNull(glTexture);
                glFramebuffer.attach(glTexture);
                return true;
            }
            return false;
        }
    }

    public EmojiFilter(Context context, PAGFile pAGFile) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.pagFile = pAGFile;
        this.handle = new Handler(Looper.getMainLooper());
        this.state1 = new State(null, false, 1, null);
        this.state2 = new State(null, false, 1, null);
        this.state3 = new State(null, false, 1, null);
        this.state4 = new State(null, false, 1, null);
        this.pagState = new State(new PagFilter(), false);
        this.TAG = "MyFilter";
        this.defaultState = new State(null, false, 3, null);
        State state = new State(null, false, 1, null);
        this.defaultInputState = state;
        this.listState = CollectionsKt.mutableListOf(this.state1, this.state2, this.state3, this.state4, this.pagState, this.defaultState, state);
        this.pagPlayer = new PAGPlayer();
        this.status = Status.PREPARE;
        if (this.pagFile == null) {
            this.pagFile = PAGFile.Load(context.getAssets(), "Start_Animation_2.pag");
        }
        this.lastTime = -1L;
        this.timeSpace = 16000.0f;
        this.isDrawPag = true;
    }

    public /* synthetic */ EmojiFilter(Context context, PAGFile pAGFile, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : pAGFile);
    }

    public final Context getContext() {
        return this.context;
    }

    public final PAGFile getPagFile() {
        return this.pagFile;
    }

    public final void setPagFile(PAGFile pAGFile) {
        this.pagFile = pAGFile;
    }

    private final PAGPlayer getPagPlayer() {
        return (PAGPlayer) this.pagPlayer;
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void onCreate(int i) {
        if (this.pagFile != null) {
            getPagPlayer().setComposition(this.pagFile);
        }
        getPagPlayer().setScaleMode(3);
        for (State state : this.listState) {
            state.onCreate();
        }
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void onDestroy() {
        for (State state : this.listState) {
            state.release();
        }
    }

    private final void updateProgressValue() {
        if (this.status == Status.RECODING) {
            this.progress = ((float) (System.currentTimeMillis() - this.lastTime)) / this.timeSpace;
        }
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
            if (this.status == Status.RECODING) {
                this.status = Status.END;
                this.handle.post(new Runnable() { // from class: com.volio.vn.b1_project.ui.recorder_video.emoji_fillter.EmojiFilter$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        EmojiFilter.$r8$lambda$e5y7GarrupkbrUoPYi6RtF0QIlg(EmojiFilter.this);
                    }
                });
            }
        }
    }

    /* renamed from: updateProgressValue$lambda-2 */
    public static final void m562updateProgressValue$lambda2(EmojiFilter this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Callback callback = this$0.callback;
        if (callback != null) {
            callback.onEnd();
        }
    }

    public final void start() {
        this.lastTime = System.currentTimeMillis();
        this.status = Status.RECODING;
        this.handle.post(new Runnable() { // from class: com.volio.vn.b1_project.ui.recorder_video.emoji_fillter.EmojiFilter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                EmojiFilter.$r8$lambda$1KIM4pF6CgprFNSvSU1BUhNcgQQ(EmojiFilter.this);
            }
        });
    }

    /* renamed from: start$lambda-3 */
    public static final void m561start$lambda3(EmojiFilter this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Callback callback = this$0.callback;
        if (callback != null) {
            callback.onStart();
        }
    }

    public final void setDrawPag(boolean z) {
        this.isDrawPag = z;
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void draw(long j, float[] transformMatrix) {
        Intrinsics.checkNotNullParameter(transformMatrix, "transformMatrix");
        createFrameBuffer();
        updateProgressValue();
        float f = this.progress;
        if (f > 0.225d && f < 0.24375f) {
            this.state1.useProgram();
            this.state1.bindBuffer();
            this.state1.draw(j, transformMatrix);
        }
        float f2 = this.progress;
        if (f2 > 0.39375d && f2 < 0.4125d) {
            this.state2.useProgram();
            this.state2.bindBuffer();
            this.state2.draw(j, transformMatrix);
        }
        float f3 = this.progress;
        if (f3 > 0.56875d && f3 < 0.5875d) {
            this.state3.useProgram();
            this.state3.bindBuffer();
            this.state3.draw(j, transformMatrix);
        }
        float f4 = this.progress;
        if (f4 > 0.73125d && f4 < 0.75d) {
            this.state4.useProgram();
            this.state4.bindBuffer();
            this.state4.draw(j, transformMatrix);
        }
        this.defaultInputState.useProgram();
        this.defaultInputState.bindBuffer();
        this.defaultInputState.draw(j, transformMatrix);
        getPagPlayer().setProgress(this.progress);
        getPagPlayer().flush();
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glEnable(3042);
        GLES20.glBlendFuncSeparate(770, 771, 1, 1);
        this.defaultState.useProgram();
        this.defaultInputState.bindTexture();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.defaultState.draw(j, Egloo.IDENTITY_MATRIX);
        this.pagState.useProgram();
        this.pagState.bindTexture();
        this.pagState.draw(j, Egloo.IDENTITY_MATRIX);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUseProgram(0);
    }

    private final void createFrameBuffer() {
        long currentTimeMillis = System.currentTimeMillis();
        for (State state : this.listState) {
            if (!Intrinsics.areEqual(state, this.pagState) && !Intrinsics.areEqual(state, this.state1) && !Intrinsics.areEqual(state, this.state2) && !Intrinsics.areEqual(state, this.state3) && !Intrinsics.areEqual(state, this.state4)) {
                state.createFrameBuffer();
            }
        }
        if (this.pagState.createFrameBuffer()) {
            Log.d(this.TAG, "createFrameBuffer: pag");
            GlTexture outputTexture = this.pagState.getOutputTexture();
            Integer valueOf = outputTexture != null ? Integer.valueOf(outputTexture.getId()) : null;
            Intrinsics.checkNotNull(valueOf);
            this.pagSurface = PAGSurface.FromTexture(valueOf.intValue(), this.pagState.getSize().getWidth(), this.pagState.getSize().getHeight(), true);
            getPagPlayer().setSurface(this.pagSurface);
        }
        if (this.state1.createFrameBuffer()) {
            Log.d(this.TAG, "createFrameBuffer: 1");
            PAGFile pAGFile = this.pagFile;
            if (pAGFile != null) {
                GlTexture outputTexture2 = this.state1.getOutputTexture();
                Intrinsics.checkNotNull(outputTexture2);
                PAGImage FromTexture = PAGImage.FromTexture(outputTexture2.getId(), 3553, this.state1.getSize().getWidth(), this.state1.getSize().getHeight(), true);
                FromTexture.setScaleMode(3);
                Unit unit = Unit.INSTANCE;
                pAGFile.replaceImage(57, FromTexture);
            }
        }
        if (this.state2.createFrameBuffer()) {
            Log.d(this.TAG, "createFrameBuffer: 1");
            PAGFile pAGFile2 = this.pagFile;
            if (pAGFile2 != null) {
                GlTexture outputTexture3 = this.state2.getOutputTexture();
                Intrinsics.checkNotNull(outputTexture3);
                PAGImage FromTexture2 = PAGImage.FromTexture(outputTexture3.getId(), 3553, this.state2.getSize().getWidth(), this.state2.getSize().getHeight(), true);
                FromTexture2.setScaleMode(3);
                Unit unit2 = Unit.INSTANCE;
                pAGFile2.replaceImage(39, FromTexture2);
            }
        }
        State state2 = this.state3;
        if (state2.createFrameBuffer()) {
            Log.d(this.TAG, "createFrameBuffer: 1");
            PAGFile pAGFile3 = this.pagFile;
            if (pAGFile3 != null) {
                GlTexture outputTexture4 = state2.getOutputTexture();
                Intrinsics.checkNotNull(outputTexture4);
                PAGImage FromTexture3 = PAGImage.FromTexture(outputTexture4.getId(), 3553, state2.getSize().getWidth(), state2.getSize().getHeight(), true);
                FromTexture3.setScaleMode(3);
                Unit unit3 = Unit.INSTANCE;
                pAGFile3.replaceImage(23, FromTexture3);
            }
        }
        State state3 = this.state4;
        if (state3.createFrameBuffer()) {
            Log.d(this.TAG, "createFrameBuffer: 1");
            PAGFile pAGFile4 = this.pagFile;
            if (pAGFile4 != null) {
                GlTexture outputTexture5 = state3.getOutputTexture();
                Intrinsics.checkNotNull(outputTexture5);
                PAGImage FromTexture4 = PAGImage.FromTexture(outputTexture5.getId(), 3553, state3.getSize().getWidth(), state3.getSize().getHeight(), true);
                FromTexture4.setScaleMode(3);
                Unit unit4 = Unit.INSTANCE;
                pAGFile4.replaceImage(16, FromTexture4);
            }
        }
        String str = this.TAG;
        StringBuilder sb = new StringBuilder("createFrameBufferSSS:");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(' ');
        sb.append(getPagPlayer().hashCode());
        sb.append(' ');
        PAGFile pAGFile5 = this.pagFile;
        sb.append(pAGFile5 != null ? pAGFile5.hashCode() : 0);
        Log.d(str, sb.toString());
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void setSize(int i, int i2) {
        for (State state : this.listState) {
            state.setSize(i, i2);
        }
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public Filter copy() {
        EmojiFilter emojiFilter = new EmojiFilter(this.context, this.pagFile);
        emojiFilter.start();
        return emojiFilter;
    }

    public final void reset() {
        this.progress = 0.0f;
        this.status = Status.PREPARE;
    }

    public final void setCallback(Callback emojiCallback) {
        Intrinsics.checkNotNullParameter(emojiCallback, "emojiCallback");
        this.callback = emojiCallback;
    }
}
