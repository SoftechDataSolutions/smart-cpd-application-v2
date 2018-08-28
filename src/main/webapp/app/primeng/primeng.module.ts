import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SmartCpdButtonDemoModule } from './buttons/button/buttondemo.module';
import { SmartCpdSplitbuttonDemoModule } from './buttons/splitbutton/splitbuttondemo.module';

import { SmartCpdDialogDemoModule } from './overlay/dialog/dialogdemo.module';
import { SmartCpdConfirmDialogDemoModule } from './overlay/confirmdialog/confirmdialogdemo.module';
import { SmartCpdLightboxDemoModule } from './overlay/lightbox/lightboxdemo.module';
import { SmartCpdTooltipDemoModule } from './overlay/tooltip/tooltipdemo.module';
import { SmartCpdOverlayPanelDemoModule } from './overlay/overlaypanel/overlaypaneldemo.module';
import { SmartCpdSideBarDemoModule } from './overlay/sidebar/sidebardemo.module';

import { SmartCpdKeyFilterDemoModule } from './inputs/keyfilter/keyfilterdemo.module';
import { SmartCpdInputTextDemoModule } from './inputs/inputtext/inputtextdemo.module';
import { SmartCpdInputTextAreaDemoModule } from './inputs/inputtextarea/inputtextareademo.module';
import { SmartCpdInputGroupDemoModule } from './inputs/inputgroup/inputgroupdemo.module';
import { SmartCpdCalendarDemoModule } from './inputs/calendar/calendardemo.module';
import { SmartCpdCheckboxDemoModule } from './inputs/checkbox/checkboxdemo.module';
import { SmartCpdChipsDemoModule } from './inputs/chips/chipsdemo.module';
import { SmartCpdColorPickerDemoModule } from './inputs/colorpicker/colorpickerdemo.module';
import { SmartCpdInputMaskDemoModule } from './inputs/inputmask/inputmaskdemo.module';
import { SmartCpdInputSwitchDemoModule } from './inputs/inputswitch/inputswitchdemo.module';
import { SmartCpdPasswordIndicatorDemoModule } from './inputs/passwordindicator/passwordindicatordemo.module';
import { SmartCpdAutoCompleteDemoModule } from './inputs/autocomplete/autocompletedemo.module';
import { SmartCpdSliderDemoModule } from './inputs/slider/sliderdemo.module';
import { SmartCpdSpinnerDemoModule } from './inputs/spinner/spinnerdemo.module';
import { SmartCpdRatingDemoModule } from './inputs/rating/ratingdemo.module';
import { SmartCpdSelectDemoModule } from './inputs/select/selectdemo.module';
import { SmartCpdSelectButtonDemoModule } from './inputs/selectbutton/selectbuttondemo.module';
import { SmartCpdListboxDemoModule } from './inputs/listbox/listboxdemo.module';
import { SmartCpdRadioButtonDemoModule } from './inputs/radiobutton/radiobuttondemo.module';
import { SmartCpdToggleButtonDemoModule } from './inputs/togglebutton/togglebuttondemo.module';
import { SmartCpdEditorDemoModule } from './inputs/editor/editordemo.module';

import { SmartCpdGrowlDemoModule } from './messages/growl/growldemo.module';
import { SmartCpdMessagesDemoModule } from './messages/messages/messagesdemo.module';
import { SmartCpdGalleriaDemoModule } from './multimedia/galleria/galleriademo.module';

import { SmartCpdFileUploadDemoModule } from './fileupload/fileupload/fileuploaddemo.module';

import { SmartCpdAccordionDemoModule } from './panel/accordion/accordiondemo.module';
import { SmartCpdPanelDemoModule } from './panel/panel/paneldemo.module';
import { SmartCpdTabViewDemoModule } from './panel/tabview/tabviewdemo.module';
import { SmartCpdFieldsetDemoModule } from './panel/fieldset/fieldsetdemo.module';
import { SmartCpdToolbarDemoModule } from './panel/toolbar/toolbardemo.module';
import { SmartCpdGridDemoModule } from './panel/grid/griddemo.module';
import { SmartCpdScrollPanelDemoModule } from './panel/scrollpanel/scrollpaneldemo.module';
import { SmartCpdCardDemoModule } from './panel/card/carddemo.module';

import { SmartCpdDataTableDemoModule } from './data/datatable/datatabledemo.module';
import { SmartCpdTableDemoModule } from './data/table/tabledemo.module';
import { SmartCpdDataGridDemoModule } from './data/datagrid/datagriddemo.module';
import { SmartCpdDataListDemoModule } from './data/datalist/datalistdemo.module';
import { SmartCpdDataScrollerDemoModule } from './data/datascroller/datascrollerdemo.module';
import { SmartCpdPickListDemoModule } from './data/picklist/picklistdemo.module';
import { SmartCpdOrderListDemoModule } from './data/orderlist/orderlistdemo.module';
import { SmartCpdScheduleDemoModule } from './data/schedule/scheduledemo.module';
import { SmartCpdTreeDemoModule } from './data/tree/treedemo.module';
import { SmartCpdTreeTableDemoModule } from './data/treetable/treetabledemo.module';
import { SmartCpdPaginatorDemoModule } from './data/paginator/paginatordemo.module';
import { SmartCpdGmapDemoModule } from './data/gmap/gmapdemo.module';
import { SmartCpdOrgChartDemoModule } from './data/orgchart/orgchartdemo.module';
import { SmartCpdCarouselDemoModule } from './data/carousel/carouseldemo.module';
import { SmartCpdDataViewDemoModule } from './data/dataview/dataviewdemo.module';

import { SmartCpdBarchartDemoModule } from './charts/barchart/barchartdemo.module';
import { SmartCpdDoughnutchartDemoModule } from './charts/doughnutchart/doughnutchartdemo.module';
import { SmartCpdLinechartDemoModule } from './charts/linechart/linechartdemo.module';
import { SmartCpdPiechartDemoModule } from './charts/piechart/piechartdemo.module';
import { SmartCpdPolarareachartDemoModule } from './charts/polarareachart/polarareachartdemo.module';
import { SmartCpdRadarchartDemoModule } from './charts/radarchart/radarchartdemo.module';

import { SmartCpdDragDropDemoModule } from './dragdrop/dragdrop/dragdropdemo.module';

import { SmartCpdMenuDemoModule } from './menu/menu/menudemo.module';
import { SmartCpdContextMenuDemoModule } from './menu/contextmenu/contextmenudemo.module';
import { SmartCpdPanelMenuDemoModule } from './menu/panelmenu/panelmenudemo.module';
import { SmartCpdStepsDemoModule } from './menu/steps/stepsdemo.module';
import { SmartCpdTieredMenuDemoModule } from './menu/tieredmenu/tieredmenudemo.module';
import { SmartCpdBreadcrumbDemoModule } from './menu/breadcrumb/breadcrumbdemo.module';
import { SmartCpdMegaMenuDemoModule } from './menu/megamenu/megamenudemo.module';
import { SmartCpdMenuBarDemoModule } from './menu/menubar/menubardemo.module';
import { SmartCpdSlideMenuDemoModule } from './menu/slidemenu/slidemenudemo.module';
import { SmartCpdTabMenuDemoModule } from './menu/tabmenu/tabmenudemo.module';

import { SmartCpdBlockUIDemoModule } from './misc/blockui/blockuidemo.module';
import { SmartCpdCaptchaDemoModule } from './misc/captcha/captchademo.module';
import { SmartCpdDeferDemoModule } from './misc/defer/deferdemo.module';
import { SmartCpdInplaceDemoModule } from './misc/inplace/inplacedemo.module';
import { SmartCpdProgressBarDemoModule } from './misc/progressbar/progressbardemo.module';
import { SmartCpdRTLDemoModule } from './misc/rtl/rtldemo.module';
import { SmartCpdTerminalDemoModule } from './misc/terminal/terminaldemo.module';
import { SmartCpdValidationDemoModule } from './misc/validation/validationdemo.module';
import { SmartCpdProgressSpinnerDemoModule } from './misc/progressspinner/progressspinnerdemo.module';

@NgModule({
    imports: [
        SmartCpdMenuDemoModule,
        SmartCpdContextMenuDemoModule,
        SmartCpdPanelMenuDemoModule,
        SmartCpdStepsDemoModule,
        SmartCpdTieredMenuDemoModule,
        SmartCpdBreadcrumbDemoModule,
        SmartCpdMegaMenuDemoModule,
        SmartCpdMenuBarDemoModule,
        SmartCpdSlideMenuDemoModule,
        SmartCpdTabMenuDemoModule,

        SmartCpdBlockUIDemoModule,
        SmartCpdCaptchaDemoModule,
        SmartCpdDeferDemoModule,
        SmartCpdInplaceDemoModule,
        SmartCpdProgressBarDemoModule,
        SmartCpdInputMaskDemoModule,
        SmartCpdRTLDemoModule,
        SmartCpdTerminalDemoModule,
        SmartCpdValidationDemoModule,

        SmartCpdButtonDemoModule,
        SmartCpdSplitbuttonDemoModule,

        SmartCpdInputTextDemoModule,
        SmartCpdInputTextAreaDemoModule,
        SmartCpdInputGroupDemoModule,
        SmartCpdCalendarDemoModule,
        SmartCpdChipsDemoModule,
        SmartCpdInputMaskDemoModule,
        SmartCpdInputSwitchDemoModule,
        SmartCpdPasswordIndicatorDemoModule,
        SmartCpdAutoCompleteDemoModule,
        SmartCpdSliderDemoModule,
        SmartCpdSpinnerDemoModule,
        SmartCpdRatingDemoModule,
        SmartCpdSelectDemoModule,
        SmartCpdSelectButtonDemoModule,
        SmartCpdListboxDemoModule,
        SmartCpdRadioButtonDemoModule,
        SmartCpdToggleButtonDemoModule,
        SmartCpdEditorDemoModule,
        SmartCpdColorPickerDemoModule,
        SmartCpdCheckboxDemoModule,
        SmartCpdKeyFilterDemoModule,

        SmartCpdGrowlDemoModule,
        SmartCpdMessagesDemoModule,
        SmartCpdGalleriaDemoModule,

        SmartCpdFileUploadDemoModule,

        SmartCpdAccordionDemoModule,
        SmartCpdPanelDemoModule,
        SmartCpdTabViewDemoModule,
        SmartCpdFieldsetDemoModule,
        SmartCpdToolbarDemoModule,
        SmartCpdGridDemoModule,
        SmartCpdScrollPanelDemoModule,
        SmartCpdCardDemoModule,

        SmartCpdBarchartDemoModule,
        SmartCpdDoughnutchartDemoModule,
        SmartCpdLinechartDemoModule,
        SmartCpdPiechartDemoModule,
        SmartCpdPolarareachartDemoModule,
        SmartCpdRadarchartDemoModule,

        SmartCpdDragDropDemoModule,

        SmartCpdDialogDemoModule,
        SmartCpdConfirmDialogDemoModule,
        SmartCpdLightboxDemoModule,
        SmartCpdTooltipDemoModule,
        SmartCpdOverlayPanelDemoModule,
        SmartCpdSideBarDemoModule,

        SmartCpdDataTableDemoModule,
        SmartCpdTableDemoModule,
        SmartCpdDataGridDemoModule,
        SmartCpdDataListDemoModule,
        SmartCpdDataViewDemoModule,
        SmartCpdDataScrollerDemoModule,
        SmartCpdScheduleDemoModule,
        SmartCpdOrderListDemoModule,
        SmartCpdPickListDemoModule,
        SmartCpdTreeDemoModule,
        SmartCpdTreeTableDemoModule,
        SmartCpdPaginatorDemoModule,
        SmartCpdOrgChartDemoModule,
        SmartCpdGmapDemoModule,
        SmartCpdCarouselDemoModule,
        SmartCpdProgressSpinnerDemoModule
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartCpdprimengModule {}
