package com.github.lppedd.backtick;

import java.lang.ref.Reference;
import java.util.ResourceBundle;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import com.intellij.BundleBase;
import com.intellij.reference.SoftReference;

/**
 * @author Edoardo Luppi
 */
public class BacktickBundle {
  @Nullable
  private static Reference<ResourceBundle> ourBundle;

  @NonNls
  public static final String BUNDLE = "messages.BacktickBundle";

  private BacktickBundle() {
    throw new UnsupportedOperationException("Cannot instantiate this class");
  }

  public static String message(
      @NotNull @PropertyKey(resourceBundle = BUNDLE) final String key,
      @NotNull final Object... params) {
    return BundleBase.message(getBundle(), key, params);
  }

  private static ResourceBundle getBundle() {
    // noinspection StaticVariableUsedBeforeInitialization
    var bundle = SoftReference.dereference(ourBundle);

    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE);
      ourBundle = new SoftReference<>(bundle);
    }

    return bundle;
  }
}
