package flaxbeard.cyberware.common.item;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import flaxbeard.cyberware.Cyberware;
import flaxbeard.cyberware.common.CyberwareContent;

import javax.annotation.Nullable;

public class ItemExpCapsule extends Item
{
	public ItemExpCapsule(String name)
	{
		this.setRegistryName(name);
		ForgeRegistries.ITEMS.register(this);
		this.setUnlocalizedName(Cyberware.MODID + "." + name);
		
		this.setCreativeTab(Cyberware.creativeTab);
				
		this.setMaxDamage(0);
		this.setMaxStackSize(1);

		CyberwareContent.items.add(this);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		if (this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("xp", 100);
			stack.setTagCompound(compound);
			list.add(stack);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);

		int xp = 0;
		if (stack.hasTagCompound())
		{
			NBTTagCompound c = stack.getTagCompound();
			if (c.hasKey("xp"))
			{
				xp = c.getInteger("xp");
			}
		}

		if (!playerIn.capabilities.isCreativeMode)
		{
			stack.shrink(1);
		}

		playerIn.addExperience(xp);

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		int xp = 0;
		if (stack.hasTagCompound())
		{
			NBTTagCompound c = stack.getTagCompound();
			if (c.hasKey("xp"))
			{
				xp = c.getInteger("xp");
			}
		}
		String before = I18n.format("cyberware.tooltip.exp_capsule.before");
		if (before.length() > 0) before = before += " ";
		
		String after = I18n.format("cyberware.tooltip.exp_capsule.after");
		if (after.length() > 0) after = " " + after;
		
		tooltip.add(ChatFormatting.RED + before + xp + after);
	}
}
