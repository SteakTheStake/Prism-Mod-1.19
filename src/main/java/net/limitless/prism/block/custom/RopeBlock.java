package net.limitless.prism.block.custom;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class RopeBlock extends PillarBlock implements Waterloggable {
    public static final BooleanProperty WATERLOGGED;
    protected static final float field_31054 = 6.5F;
    protected static final float field_31055 = 9.5F;
    protected static final VoxelShape Y_SHAPE;
    protected static final VoxelShape Z_SHAPE;
    protected static final VoxelShape X_SHAPE;

    public RopeBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(WATERLOGGED, false)).with(AXIS, Axis.Y));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch((Axis)state.get(AXIS)) {
            case X:
            default:
                return X_SHAPE;
            case Z:
                return Z_SHAPE;
            case Y:
                return Y_SHAPE;
        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return (BlockState)super.getPlacementState(ctx).with(WATERLOGGED, bl);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(new Property[]{WATERLOGGED}).add(new Property[]{AXIS});
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        Y_SHAPE = Block.createCuboidShape(6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D);
        Z_SHAPE = Block.createCuboidShape(6.5D, 6.5D, 0.0D, 9.5D, 9.5D, 16.0D);
        X_SHAPE = Block.createCuboidShape(0.0D, 6.5D, 6.5D, 16.0D, 9.5D, 9.5D);
    }
}
