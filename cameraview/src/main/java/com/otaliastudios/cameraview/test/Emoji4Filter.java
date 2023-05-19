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
