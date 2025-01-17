import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { PanelModule } from 'primeng/panel';

import { DotMessagePipeModule } from '@dotcms/ui';

import { DotPagesCardModule } from './dot-pages-card/dot-pages-card.module';
import { DotPagesFavoritePanelComponent } from './dot-pages-favorite-panel.component';

@NgModule({
    imports: [CommonModule, DotMessagePipeModule, DotPagesCardModule, PanelModule, ButtonModule],
    declarations: [DotPagesFavoritePanelComponent],
    exports: [DotPagesFavoritePanelComponent]
})
export class DotPagesFavoritePanelModule {}
