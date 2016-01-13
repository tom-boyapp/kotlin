/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.scopes.BaseImportingScope
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import org.jetbrains.kotlin.resolve.scopes.ResolutionScope
import org.jetbrains.kotlin.utils.Printer

class AllUnderImportScope(
        descriptor: DeclarationDescriptor,
        aliasImportNames: Collection<FqName>
) : BaseImportingScope(null) {

    private val scopes: List<ResolutionScope> = if (descriptor is ClassDescriptor) {
        listOf(descriptor.staticScope, descriptor.unsubstitutedInnerClassesScope)
    }
    else {
        assert(descriptor is PackageViewDescriptor) {
            "Must be class or package view descriptor: $descriptor"
        }
        listOf((descriptor as PackageViewDescriptor).memberScope)
    }

    private val excludedNames = if (aliasImportNames.isEmpty()) { // optimization
        emptyList<Name>()
    }
    else {
        val fqName = DescriptorUtils.getFqNameSafe(descriptor)
        aliasImportNames.mapNotNull { if (it.parent() == fqName) it.shortName() else null }
    }

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): List<DeclarationDescriptor> {
        val nameFilterToUse = if (excludedNames.isEmpty()) { // optimization
            nameFilter
        }
        else {
            { it !in excludedNames && nameFilter(it) }
        }

        return scopes.flatMap { it.getContributedDescriptors(kindFilter, nameFilterToUse) }
    }

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? {
        if (name in excludedNames) return null
        return scopes.asSequence().mapNotNull { it.getContributedClassifier(name, location) }.singleOrNull()
    }

    override fun getContributedVariables(name: Name, location: LookupLocation): List<VariableDescriptor> {
        if (name in excludedNames) return emptyList()
        return scopes.flatMap { it.getContributedVariables(name, location) }
    }

    override fun getContributedFunctions(name: Name, location: LookupLocation): List<FunctionDescriptor> {
        if (name in excludedNames) return emptyList()
        return scopes.flatMap { it.getContributedFunctions(name, location) }
    }

    override fun printStructure(p: Printer) {
        p.println(javaClass.simpleName)
    }
}

