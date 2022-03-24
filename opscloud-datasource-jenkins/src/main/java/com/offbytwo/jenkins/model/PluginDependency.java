package com.offbytwo.jenkins.model;

/**
 * @author Karl Heinz Marbaise
 */
public class PluginDependency extends BaseModel
{
    private boolean optional;

    private String shortName;

    private String version;

    public boolean isOptional() {
        return optional;
    }

    public PluginDependency setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public PluginDependency setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PluginDependency setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( optional ? 1231 : 1237 );
        result = prime * result + ( ( shortName == null ) ? 0 : shortName.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        PluginDependency other = (PluginDependency) obj;
        if ( optional != other.optional )
            return false;
        if ( shortName == null )
        {
            if ( other.shortName != null )
                return false;
        }
        else if ( !shortName.equals( other.shortName ) )
            return false;
        if ( version == null )
        {
            if ( other.version != null )
                return false;
        }
        else if ( !version.equals( other.version ) )
            return false;
        return true;
    }

}
