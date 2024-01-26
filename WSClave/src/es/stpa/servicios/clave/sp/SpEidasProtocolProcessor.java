package es.stpa.servicios.clave.sp;

import org.apache.commons.lang.StringUtils;

import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.commons.EidasErrors;
import eu.eidas.auth.commons.PersonalAttribute;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.AttributeRegistry;
import eu.eidas.auth.commons.attribute.PersonType;
import eu.eidas.auth.commons.attribute.impl.StringAttributeValueMarshaller;
import eu.eidas.auth.commons.exceptions.InternalErrorEIDASException;
import eu.eidas.auth.engine.AbstractProtocolEngine;
import eu.eidas.auth.engine.ProtocolEngine;
import eu.eidas.auth.engine.core.ProtocolProcessorNoMetadataI;
import eu.eidas.auth.engine.core.eidas.EidasProtocolProcessorNoMetadata;

/**
 * The goal of this subclass is to allow any unknown requested attribute to be generated by the ServiceProvider.
 */
public final class SpEidasProtocolProcessor extends EidasProtocolProcessorNoMetadata implements ProtocolProcessorNoMetadataI {


    public SpEidasProtocolProcessor() {
        super();
    }

    public SpEidasProtocolProcessor(AttributeRegistry additionalAttributeRegistry) {
        super(additionalAttributeRegistry);
    }

    public SpEidasProtocolProcessor(String eidasAttributesFileName,
                                    String additionalAttributesFileName,
                                    String defaultPath) {
        super(eidasAttributesFileName, additionalAttributesFileName, defaultPath);
    }

    public SpEidasProtocolProcessor(AttributeRegistry eidasAttributeRegistry,
                                    AttributeRegistry additionalAttributeRegistry) {
        super(eidasAttributeRegistry, additionalAttributeRegistry);
    }

    /**
     * For the SP, we create unknown attributes on the fly so this method never returns {@code null}.
     *
     * @param name the full name URI of the attribute to search for
     * @return an existing attribute from the registries or a new attribute created on the fly
     */
    @Override
    public AttributeDefinition<?> getAttributeDefinitionNullable(String name) {
        if (StringUtils.isBlank(name)) {
            throw new InternalErrorEIDASException(EidasErrors.get(EidasErrorKey.INTERNAL_ERROR.errorCode()),
                                                  ProtocolEngine.ATTRIBUTE_EMPTY_LITERAL);
        }
        AttributeDefinition<?> attributeDefinition = getMinimumDataSetAttributes().getByName(name);
        if (null != attributeDefinition) {
            return attributeDefinition;
        }
        attributeDefinition = getAdditionalAttributes().getByName(name);
        if (null != attributeDefinition) {
            return attributeDefinition;
        }

        // For the SP, we create unknown attributes on the fly:
        AttributeDefinition<String> unknownAttribute = AttributeDefinition.<String>builder().nameUri(name)
                .friendlyName(PersonalAttribute.extractFriendlyName(name))
                .personType(PersonType.NATURAL_PERSON)
                .xmlType("http://www.w3.org/2001/XMLSchema", "string", "xs")
                .attributeValueMarshaller(new StringAttributeValueMarshaller())
                .build();

        return unknownAttribute;
    }
}
