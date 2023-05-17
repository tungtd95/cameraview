package com.otaliastudios.cameraview.test;

import android.content.Context;
import android.opengl.GLES20;
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

import org.libpag.PAGFile;
import org.libpag.PAGImage;
import org.libpag.PAGPlayer;
import org.libpag.PAGSurface;

import java.util.List;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;


public final class Emoji4Filter implements Filter {
    private final String TAG;
    private final Context context;
    private State defaultInputState;
    private State defaultState;
    private long lastTime;
    private List<State> listState;
    private PAGFile pagFile;
    private final PAGPlayer pagPlayer;
    private State pagState;
    private PAGSurface pagSurface;
    private float progress;
    private State state1;
    private State state2;
    private State state3;
    private State state4;
    private float timeSpace;

    @Override // com.otaliastudios.cameraview.filter.Filter
    public String getFragmentShader() {
        return GlTextureProgram.SIMPLE_FRAGMENT_SHADER;
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public String getVertexShader() {
        return GlTextureProgram.SIMPLE_VERTEX_SHADER;
    }

    public Emoji4Filter(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
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

        pagFile = PAGFile.Load(context.getAssets(), "Start_Animation_2.pag");
        this.pagPlayer.setComposition(Emoji4Filter.this.pagFile);
        this.pagPlayer.setScaleMode(3);

        this.lastTime = -1L;
        this.timeSpace = 16000.0f;
    }

    public final Context getContext() {
        return this.context;
    }


    /* compiled from: Emoji4Filter.kt */
    @Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010'\u001a\u00020(J\u0006\u0010)\u001a\u00020(J\u0006\u0010*\u001a\u00020\u0005J\u0016\u0010+\u001a\u00020(2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/J\u0006\u00100\u001a\u00020(J\u0006\u00101\u001a\u00020(J\u0006\u00102\u001a\u00020(J\u0016\u0010%\u001a\u00020(2\u0006\u00103\u001a\u00020\u001c2\u0006\u00104\u001a\u00020\u001cJ\u0006\u00105\u001a\u00020(R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u001cX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\"X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&¨\u00066"}, d2 = {"Lcom/volio/vn/b1_project/ui/recorder_video/emoji_fillter/Emoji4Filter$State;", "", TextureMediaEncoder.FILTER_EVENT, "Lcom/otaliastudios/cameraview/filter/BaseFilter;", "isSampler2D", "", "(Lcom/otaliastudios/cameraview/filter/BaseFilter;Z)V", "getFilter", "()Lcom/otaliastudios/cameraview/filter/BaseFilter;", "setFilter", "(Lcom/otaliastudios/cameraview/filter/BaseFilter;)V", "isChangeSize", "()Z", "setChangeSize", "(Z)V", "outputFramebuffer", "Lcom/otaliastudios/opengl/texture/GlFramebuffer;", "getOutputFramebuffer", "()Lcom/otaliastudios/opengl/texture/GlFramebuffer;", "setOutputFramebuffer", "(Lcom/otaliastudios/opengl/texture/GlFramebuffer;)V", "outputTexture", "Lcom/otaliastudios/opengl/texture/GlTexture;", "getOutputTexture", "()Lcom/otaliastudios/opengl/texture/GlTexture;", "setOutputTexture", "(Lcom/otaliastudios/opengl/texture/GlTexture;)V", "program", "", "getProgram", "()I", "setProgram", "(I)V", "size", "Lcom/otaliastudios/cameraview/size/Size;", "getSize", "()Lcom/otaliastudios/cameraview/size/Size;", "setSize", "(Lcom/otaliastudios/cameraview/size/Size;)V", "bindBuffer", "", "bindTexture", "createFrameBuffer", "draw", "timestampUs", "", "transformMatrix", "", "onCreate", "release", "releaseBufferTexture", "width", "height", "useProgram", "B1_emoji_video_1.0.1_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
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

        public State(BaseFilter filter, boolean z) {
            Intrinsics.checkNotNullParameter(filter, "filter");
            this.filter = filter;
            this.isSampler2D = z;
            this.size = new Size(1, 1);
            this.program = -1;
        }

        public State(NoFilter noFilter, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
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
                this.outputTexture = new GlTexture(33984, 3553, this.size.getWidth(), this.size.getHeight());
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

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void onCreate(int i) {
        for (State state : this.listState) {
            state.onCreate();
        }
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void onDestroy() {
        for (State state : this.listState) {
            state.release();
        }
        PAGSurface pAGSurface = this.pagSurface;
        if (pAGSurface != null) {
            pAGSurface.clearAll();
        }
        PAGSurface pAGSurface2 = this.pagSurface;
        if (pAGSurface2 != null) {
            pAGSurface2.freeCache();
        }
        PAGSurface pAGSurface3 = this.pagSurface;
        if (pAGSurface3 != null) {
            pAGSurface3.release();
        }
        this.pagPlayer.release();
    }

    private final void updateProgressValue() {
        if (this.lastTime == -1) {
            this.lastTime = System.currentTimeMillis();
            this.progress = 0.0f;
        }
        float currentTimeMillis = ((float) (System.currentTimeMillis() - this.lastTime)) / this.timeSpace;
        this.progress = currentTimeMillis;
        if (currentTimeMillis >= 1.0f) {
            this.progress = 1.0f;
        }
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
        this.pagPlayer.setProgress(this.progress);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder("draw: ");
        sb.append(this.progress);
        Log.d(str, sb.toString());
        this.pagPlayer.flush();
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
        GLES20.glBindTexture(3553, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glDisable(3042);
    }

    private final void createFrameBuffer() {
        PAGFile pAGFile;
        PAGFile pAGFile2;
        PAGFile pAGFile3;
        PAGFile pAGFile4;
        for (State state : this.listState) {
            if (!Intrinsics.areEqual(state, this.pagState) && !Intrinsics.areEqual(state, this.state1) && !Intrinsics.areEqual(state, this.state2) && !Intrinsics.areEqual(state, this.state3) && !Intrinsics.areEqual(state, this.state4)) {
                state.createFrameBuffer();
            }
        }
        if (this.pagState.createFrameBuffer()) {
            GlTexture outputTexture = this.pagState.getOutputTexture();
            Integer valueOf = outputTexture != null ? Integer.valueOf(outputTexture.getId()) : null;
            Intrinsics.checkNotNull(valueOf);
            PAGSurface FromTexture = PAGSurface.FromTexture(valueOf.intValue(), this.pagState.getSize().getWidth(), this.pagState.getSize().getHeight(), true);
            this.pagSurface = FromTexture;
            this.pagPlayer.setSurface(FromTexture);
        }
        State state2 = this.state1;
        if (state2.createFrameBuffer() && (pAGFile4 = this.pagFile) != null) {
            GlTexture outputTexture2 = state2.getOutputTexture();
            Intrinsics.checkNotNull(outputTexture2);
            PAGImage FromTexture2 = PAGImage.FromTexture(outputTexture2.getId(), 3553, state2.getSize().getWidth(), state2.getSize().getHeight(), true);
            FromTexture2.setScaleMode(3);
            Unit unit = Unit.INSTANCE;
            pAGFile4.replaceImage(57, FromTexture2);
        }
        State state3 = this.state2;
        if (state3.createFrameBuffer() && (pAGFile3 = this.pagFile) != null) {
            GlTexture outputTexture3 = state3.getOutputTexture();
            Intrinsics.checkNotNull(outputTexture3);
            PAGImage FromTexture3 = PAGImage.FromTexture(outputTexture3.getId(), 3553, state3.getSize().getWidth(), state3.getSize().getHeight(), true);
            FromTexture3.setScaleMode(3);
            Unit unit2 = Unit.INSTANCE;
            pAGFile3.replaceImage(39, FromTexture3);
        }
        State state4 = this.state3;
        if (state4.createFrameBuffer() && (pAGFile2 = this.pagFile) != null) {
            GlTexture outputTexture4 = state4.getOutputTexture();
            Intrinsics.checkNotNull(outputTexture4);
            PAGImage FromTexture4 = PAGImage.FromTexture(outputTexture4.getId(), 3553, state4.getSize().getWidth(), state4.getSize().getHeight(), true);
            FromTexture4.setScaleMode(3);
            Unit unit3 = Unit.INSTANCE;
            pAGFile2.replaceImage(23, FromTexture4);
        }
        State state5 = this.state4;
        if (!state5.createFrameBuffer() || (pAGFile = this.pagFile) == null) {
            return;
        }
        GlTexture outputTexture5 = state5.getOutputTexture();
        Intrinsics.checkNotNull(outputTexture5);
        PAGImage FromTexture5 = PAGImage.FromTexture(outputTexture5.getId(), 3553, state5.getSize().getWidth(), state5.getSize().getHeight(), true);
        FromTexture5.setScaleMode(3);
        Unit unit4 = Unit.INSTANCE;
        pAGFile.replaceImage(16, FromTexture5);
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public void setSize(int i, int i2) {
        for (State state : this.listState) {
            state.setSize(i, i2);
        }
    }

    @Override // com.otaliastudios.cameraview.filter.Filter
    public Filter copy() {
        return new Emoji4Filter(this.context);
    }

    public final void reset() {
        this.progress = 0.0f;
    }
}
