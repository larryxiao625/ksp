package com.google.devtools.ksp.symbol.impl.binary

import com.google.devtools.ksp.KSObjectCache
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.impl.kotlin.KSNameImpl
import com.google.devtools.ksp.toKSModifiers
import org.jetbrains.kotlin.descriptors.TypeAliasDescriptor

class KSTypeAliasDescriptorImpl(descriptor: TypeAliasDescriptor) :
    KSTypeAlias,
    KSDeclarationDescriptorImpl(descriptor),
    KSExpectActual by KSExpectActualDescriptorImpl(descriptor) {
    companion object : KSObjectCache<TypeAliasDescriptor, KSTypeAliasDescriptorImpl>() {
        fun getCached(descriptor: TypeAliasDescriptor) = KSTypeAliasDescriptorImpl.cache.getOrPut(descriptor) {
            KSTypeAliasDescriptorImpl(descriptor)
        }
    }

    override val name: KSName by lazy {
        KSNameImpl.getCached(descriptor.name.asString())
    }

    override val modifiers: Set<Modifier> by lazy {
        descriptor.toKSModifiers()
    }

    override val typeParameters: List<KSTypeParameter> by lazy {
        descriptor.declaredTypeParameters.map { KSTypeParameterDescriptorImpl.getCached(it) }
    }

    override val type: KSTypeReference by lazy {
        KSTypeReferenceDescriptorImpl.getCached(descriptor.underlyingType, origin, this)
    }

    override fun <D, R> accept(visitor: KSVisitor<D, R>, data: D): R {
        return visitor.visitTypeAlias(this, data)
    }
}
