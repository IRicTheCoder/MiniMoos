package com.ricardothecoder.minimoos.addons.mfr;

import com.ricardothecoder.minimoos.entities.EntityFluidMoo;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MooMessage implements IMessage
{
    private int entityID;
    private int delay;
    private int buckets;

    public MooMessage() {}

    public MooMessage(EntityFluidMoo moo)
    {
        this.entityID = moo.getEntityId();
        this.delay = moo.getDelay();
        this.buckets = moo.getBuckets();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityID = ByteBufUtils.readVarInt(buf, 4);
        this.delay = ByteBufUtils.readVarInt(buf, 4);
        this.buckets = ByteBufUtils.readVarInt(buf, 4);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeVarInt(buf, this.entityID, 4);
        ByteBufUtils.writeVarInt(buf, this.delay, 4);
        ByteBufUtils.writeVarInt(buf, this.buckets, 4);
    }

    public static class Handler implements IMessageHandler<MooMessage, IMessage>
    {
        @Override
        public IMessage onMessage(MooMessage message, MessageContext ctx)
        {
            if (ctx.side == Side.CLIENT && message != null)
            {
                World world = FMLClientHandler.instance().getWorldClient();

                if (world != null)
                {
                    Entity entity = world.getEntityByID(message.entityID);

                    if (entity != null && entity instanceof EntityFluidMoo)
                    {
                    	EntityFluidMoo moo = (EntityFluidMoo)entity;
                        moo.setDelay(message.delay);
                        moo.setBuckets(message.buckets);
                    }
                }
            }

            return null; // no response in this case
        }
    }
}